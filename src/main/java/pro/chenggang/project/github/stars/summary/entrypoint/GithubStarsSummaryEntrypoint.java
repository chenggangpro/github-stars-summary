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

import java.io.File;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    private AsynchronousFileChannel outputFileChannel;
    private final DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
    private final Set<String> alreadySummarizedList = ConcurrentHashMap.newKeySet();
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
                            .concatMap(summaryResponse -> {
                                return Mono.fromCallable(() -> {
                                            Context context = new Context();
                                            context.setVariable("summary", summaryResponse);
                                            return templateEngine.process("snippet", context);
                                        })
                                        .flatMapMany(lines -> {
                                            byte[] bytes = (lines + System.lineSeparator()).getBytes();
                                            DefaultDataBuffer wrap = factory.wrap(bytes);
                                            return Mono.fromCallable(outputFileChannel::size)
                                                    .flatMapMany(position -> DataBufferUtils.write(Mono.just(wrap),
                                                                    outputFileChannel,
                                                                    position
                                                            )
                                                    );
                                        })
                                        .then(Mono.defer(() -> {
                                            log.info("Write repository {} summary to file", summaryResponse.getRepositoryName());
                                            return this.writeLockFile(summaryResponse.getRepositoryFullName());
                                        }));
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
        String userDir = System.getProperty("user.dir");
        Path outputFilePath = new File(userDir + File.separator + "documents/GitHubStars.md").toPath();
        outputFileChannel = AsynchronousFileChannel.open(
                outputFilePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
//                    ,
//                    StandardOpenOption.TRUNCATE_EXISTING
        );
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
        if (Objects.nonNull(outputFileChannel)) {
            this.outputFileChannel.close();
        }
        if (Objects.nonNull(lockFileChannel)) {
            this.lockFileChannel.close();
        }
    }
}
