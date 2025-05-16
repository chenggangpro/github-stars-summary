/*
 *    Copyright 2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package pro.chenggang.project.github.stars.summary.entrypoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pro.chenggang.project.github.stars.summary.entity.GithubRepositoryInfo;
import pro.chenggang.project.github.stars.summary.entity.GithubRepositoryInfo.GithubRepositoryInfoBuilder;
import pro.chenggang.project.github.stars.summary.entity.Tags;
import pro.chenggang.project.github.stars.summary.github.GitHubApi;
import pro.chenggang.project.github.stars.summary.llm.LlmApi;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.substringAfter;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GithubStarsSummaryEntrypoint implements InitializingBean, DisposableBean {

    private AsynchronousFileChannel lockFileChannel;
    private AsynchronousFileChannel processingLogChannel;
    private final Map<Predicate<Collection<String>>, Tuple2<String, AsynchronousFileChannel>> channelMap = new LinkedHashMap<>();
    private final DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
    private final Set<String> alreadySummarizedList = ConcurrentHashMap.newKeySet();
    private final GitHubApi gitHubApi;
    private final LlmApi llmApi;
    private final TemplateEngine templateEngine;

    public Mono<Void> summary(int limit, int batchSize) {
        return gitHubApi.listStars(limit)
                .filter(starsRepository -> !alreadySummarizedList.contains(starsRepository.getFullName()))
                .buffer(batchSize)
                .concatMap(buffered -> {
                    return Flux.fromIterable(buffered)
                            .flatMap(starsRepository -> {
                                        return gitHubApi.getLanguageInfo(starsRepository.getLanguagesUrl())
                                                .flatMap(languageInfos -> {
                                                    return gitHubApi.getReadmeContent(starsRepository.getUrl())
                                                            .map(readmeContent -> {
                                                                byte[] decoded = Base64.getDecoder()
                                                                        .decode(readmeContent.replace("\n", ""));
                                                                return new String(decoded);
                                                            })
                                                            .defaultIfEmpty(StringUtils.defaultString(starsRepository.getDescription()))
                                                            .map(readmeContent -> {
                                                                if (StringUtils.isBlank(readmeContent)) {
                                                                    log.warn("Readme content is blank, repository: {}",
                                                                            starsRepository.getUrl()
                                                                    );
                                                                }
                                                                GithubRepositoryInfoBuilder githubRepositoryInfoBuilder = GithubRepositoryInfo.builder()
                                                                        .name(starsRepository.getName())
                                                                        .fullName(starsRepository.getFullName())
                                                                        .readmeContent(readmeContent)
                                                                        .url(starsRepository.getHtmlUrl().toString())
                                                                        .languages(languageInfos);
                                                                if (Objects.nonNull(starsRepository.getLicense())) {
                                                                    githubRepositoryInfoBuilder.license(starsRepository.getLicense()
                                                                                    .getName())
                                                                            .licenseUrl(starsRepository.getLicense().getUrl());
                                                                }
                                                                return githubRepositoryInfoBuilder.build();
                                                            });
                                                })
                                                .flatMap(llmApi::summary)
                                                .map(summaryResponse -> {
                                                    return summaryResponse.toBuilder()
                                                            .tags(summaryResponse.getTags().stream()
                                                                    .map(value -> "#" + value)
                                                                    .toList()
                                                            )
                                                            .repositoryName(starsRepository.getName())
                                                            .repositoryFullName(starsRepository.getFullName())
                                                            .repositoryUrl(starsRepository.getHtmlUrl().toString())
                                                            .repositoryName("[" + summaryResponse.getRepositoryName() + "]")
                                                            .build();
                                                });
                                    }, batchSize
                            )
                            .collectList();
                })
                .concatMap(summaryResponseList -> {
                    return Flux.fromIterable(summaryResponseList)
                            .groupBy(summaryResponse -> {
                                Set<String> tags = Optional.ofNullable(summaryResponse.getTags())
                                        .orElse(new ArrayList<>())
                                        .stream()
                                        .map(value -> substringAfter(value, "#"))
                                        .map(StringUtils::trim)
                                        .collect(Collectors.toSet());
                                return channelMap.entrySet()
                                        .stream()
                                        .filter(entry -> entry.getKey().test(tags))
                                        .findFirst()
                                        .map(Entry::getValue)
                                        .orElseGet(() -> channelMap.get(Tags.Others.getTagPredicate()));
                            })
                            .concatMap(groupedFlux -> {
                                Tuple2<String, AsynchronousFileChannel> tuple2 = groupedFlux.key();
                                String filePath = tuple2.getT1();
                                AsynchronousFileChannel asynchronousFileChannel = tuple2.getT2();
                                return groupedFlux.concatMap(summaryResponse -> {
                                    return Mono.fromCallable(() -> {
                                                Context context = new Context();
                                                context.setVariable("summary", summaryResponse);
                                                return templateEngine.process("snippet", context);
                                            })
                                            .flatMapMany(lines -> {
                                                byte[] bytes = (lines + System.lineSeparator()).getBytes();
                                                DefaultDataBuffer wrap = factory.wrap(bytes);
                                                return Mono.fromCallable(asynchronousFileChannel::size)
                                                        .flatMapMany(position -> DataBufferUtils.write(Mono.just(wrap),
                                                                        asynchronousFileChannel,
                                                                        position
                                                                )
                                                        );
                                            })
                                            .then(Mono.defer(() -> {
                                                log.info("Write repository {} summary to file",
                                                        summaryResponse.getRepositoryName()
                                                );
                                                return this.writeLockFile(summaryResponse.getRepositoryFullName());
                                            }))
                                            .then(Mono.defer(() -> {
                                                String logContent = StringUtils.rightPad(filePath,
                                                        35,
                                                        ""
                                                ) + "<===> " + summaryResponse.getTags() + System.lineSeparator();
                                                DefaultDataBuffer wrap = factory.wrap(logContent.getBytes());
                                                return Mono.fromCallable(processingLogChannel::size)
                                                        .flatMapMany(position -> DataBufferUtils.write(Mono.just(wrap),
                                                                        processingLogChannel,
                                                                        position
                                                                )
                                                        )
                                                        .then();
                                            }));
                                });
                            });
                })
                .then();
    }

    private Mono<Void> writeLockFile(String repositoryFullName) {
        return Mono.just(repositoryFullName)
                .flatMap(name -> {
                    this.alreadySummarizedList.add(repositoryFullName);
                    byte[] bytes = (name + System.lineSeparator()).getBytes();
                    DefaultDataBuffer wrap = factory.wrap(bytes);
                    return Mono.fromCallable(this.lockFileChannel::size)
                            .flatMapMany(position -> DataBufferUtils.write(Mono.just(wrap),
                                            lockFileChannel,
                                            position
                                    )
                            )
                            .then(Mono.defer(() -> {
                                log.info("Write lock file success: {}", repositoryFullName);
                                return Mono.empty();
                            }));
                });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initChannelMap();
        String userDir = System.getProperty("user.dir");
        File lockFile = new File(userDir + File.separator + "summary.lock");
        if (!lockFile.exists()) {
            lockFile.createNewFile();
        }
        Path lockFilePath = lockFile.toPath();
        Files.readAllLines(lockFilePath)
                .stream()
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .forEach(alreadySummarizedList::add);
        lockFileChannel = AsynchronousFileChannel.open(
                lockFilePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
        );
    }

    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(lockFileChannel)) {
            this.lockFileChannel.close();
        }
        channelMap.forEach((k, v) -> {
            try {
                v.getT2().close();
            } catch (IOException e) {
                //ignore
            }
        });
        if (Objects.nonNull(processingLogChannel)) {
            processingLogChannel.close();
        }
    }

    private void initChannelMap() throws IOException {
        String userDir = System.getProperty("user.dir");
        Tags[] values = Tags.values();
        for (Tags value : values) {
            AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(
                    new File(userDir + value.getPath()).toPath(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            channelMap.put(value.getTagPredicate(), Tuples.of(value.getPath(), asynchronousFileChannel));
        }
        processingLogChannel = AsynchronousFileChannel.open(
                new File(userDir + "/documents/processing.log").toPath(),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}
