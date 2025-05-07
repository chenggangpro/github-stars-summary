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
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private final DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
    private final Set<String> alreadySummarizedList = ConcurrentHashMap.newKeySet();
    private final Map<Predicate<String>, AsynchronousFileChannel> fileChannels = new ConcurrentHashMap<>();
    private final GitHubApi gitHubApi;
    private final LlmApi llmApi;
    private final TemplateEngine templateEngine;

    public Mono<Void> summary(int limit) {
        return gitHubApi.listStars(limit)
                .filter(starsRepository -> !alreadySummarizedList.contains(starsRepository.getFullName()))
                .buffer(5)
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
                                                .flatMap(llmApi::summary);
                                    }, 5
                            )
                            .map(summaryResponse -> {
                                return summaryResponse.toBuilder()
                                        .tags(summaryResponse.getTags().stream()
                                                .map(value -> "#" + value)
                                                .toList()
                                        )
                                        .repositoryName("[" + summaryResponse.getRepositoryName() + "]")
                                        .build();
                            })
                            .collectList();
                })
                .concatMap(summaryResponseList -> {
                    return Flux.fromIterable(summaryResponseList)
                            .flatMap(summaryResponse -> {
                                        return Mono.fromCallable(() -> fileChannels.entrySet()
                                                        .stream()
                                                        .filter(entry -> entry.getKey().test(summaryResponse.getRepositoryType()))
                                                        .findFirst()
                                                        .map(Map.Entry::getValue)
                                                )
                                                .flatMap(Mono::justOrEmpty)
                                                .switchIfEmpty(Mono.defer(() -> {
                                                    log.warn("Repository type not found: {}", summaryResponse.getRepositoryType());
                                                    return Mono.fromCallable(() -> fileChannels.entrySet()
                                                                    .stream()
                                                                    .filter(entry -> entry.getKey().test(RepositoryType.Others.name()))
                                                                    .findFirst()
                                                                    .map(Map.Entry::getValue)
                                                            )
                                                            .flatMap(Mono::justOrEmpty);
                                                }))
                                                .map(asynchronousFileChannel -> Tuples.of(summaryResponse, asynchronousFileChannel));
                                    }, 5
                            )
                            .groupBy(Tuple2::getT2)
                            .flatMap(groupedFlux -> {
                                AsynchronousFileChannel asynchronousFileChannel = groupedFlux.key();
                                return groupedFlux.map(Tuple2::getT1)
                                        .concatMap(summaryResponse -> {
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
                                                        log.info("Write repository ({}) summary to file: {}.md",
                                                                summaryResponse.getRepositoryName(),
                                                                summaryResponse.getRepositoryType()
                                                        );
                                                        return Mono.just(summaryResponse.getRepositoryFullName());
                                                    }));
                                        })
                                        .collectList()
                                        .flatMap(repositoryFullNameList -> {
                                            this.alreadySummarizedList.addAll(repositoryFullNameList);
                                            String lines = repositoryFullNameList.stream()
                                                    .collect(Collectors.joining(System.lineSeparator()));
                                            byte[] bytes = lines.getBytes();
                                            DefaultDataBuffer wrap = factory.wrap(bytes);
                                            return Mono.fromCallable(this.lockFileChannel::size)
                                                    .flatMapMany(position -> DataBufferUtils.write(Mono.just(wrap),
                                                                    lockFileChannel,
                                                                    position
                                                            )
                                                    )
                                                    .then(Mono.defer(() -> {
                                                        log.info("Write lock file success: {}", repositoryFullNameList);
                                                        return Mono.empty();
                                                    }));
                                        });
                            });
                })
                .then();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String userDir = System.getProperty("user.dir");
        for (RepositoryType repositoryType : RepositoryType.values()) {
            Predicate<String> predicate = new Predicate<>() {
                @Override
                public boolean test(String s) {
                    return StringUtils.equalsAnyIgnoreCase(s, repositoryType.name()) || StringUtils.startsWithIgnoreCase(
                            repositoryType.name(),
                            s
                    );
                }
            };
            Path path = new File(userDir + File.separator + repositoryType.getFileName()).toPath();
            AsynchronousFileChannel asyncChannel = AsynchronousFileChannel.open(
                    path,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
//                    ,
//                    StandardOpenOption.TRUNCATE_EXISTING
            );
            fileChannels.put(predicate, asyncChannel);
        }
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
        fileChannels.forEach((k, v) -> {
            try {
                v.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        fileChannels.clear();
        if (Objects.nonNull(lockFileChannel)) {
            this.lockFileChannel.close();
        }
    }
}
