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
package pro.chenggang.project.github.stars.summary.github;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import pro.chenggang.project.github.stars.summary.entity.GithubRepositoryInfo.LanguageInfo;
import pro.chenggang.project.github.stars.summary.github.dto.ContentTree;
import pro.chenggang.project.github.stars.summary.github.dto.StarsRepository;
import pro.chenggang.project.github.stars.summary.properties.GitHubStarsSummaryProperties;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubApi {

    @Qualifier("githubWebClient")
    private final WebClient githubWebClient;
    private final GitHubStarsSummaryProperties gitHubStarsSummaryProperties;

    /**
     * curl -L \
     * -H "Accept: application/vnd.github+json" \
     * -H "Authorization: Bearer <YOUR-TOKEN>" \
     * -H "X-GitHub-Api-Version: 2022-11-28" \
     *
     * @see <a href="https://api.github.com/user/starred" >github-user-starred</a>
     */
    public Flux<StarsRepository> listStars(int limit) {
        int perPage = 30;
        if (limit > 0 && limit <= 30) {
            perPage = limit;
        }
        return this.pagingStarsRepository(URI.create("/user/starred?per_page=" + perPage + "&direction=desc"), limit)
                .contextWrite(context -> context.put(AtomicInteger.class, new AtomicInteger(0)));
    }

    private Flux<StarsRepository> pagingStarsRepository(URI uri, int limit) {
        return Flux.deferContextual(contextView -> {
            return Mono.just(contextView.get(AtomicInteger.class))
                    .flatMapMany(counter -> {
                        if (limit > 0 && counter.get() >= limit) {
                            return Flux.empty();
                        }
                        String scheme = uri.getScheme();
                        RequestHeadersSpec<?> requestHeadersSpec;
                        if (StringUtils.isNotBlank(scheme)) {
                            requestHeadersSpec = githubWebClient.get()
                                    .uri(uri);
                        } else {
                            requestHeadersSpec = githubWebClient.get()
                                    .uri(uriBuilder -> uriBuilder.path(uri.getPath())
                                            .query(uri.getQuery())
                                            .build()
                                    );
                        }
                        return requestHeadersSpec
                                .retrieve()
                                .toEntity(new ParameterizedTypeReference<List<StarsRepository>>() {
                                })
                                .flatMapMany(responseEntity -> {
                                    HttpHeaders responseEntityHeaders = responseEntity.getHeaders();
                                    String rateLimit = responseEntityHeaders.entrySet()
                                            .stream()
                                            .filter(entry -> entry.getKey().startsWith("X-RateLimit"))
                                            .map(entry -> StringUtils.substringAfter(entry.getKey(), "X-RateLimit-")
                                                    + ": " + entry.getValue()
                                            )
                                            .collect(Collectors.joining(","));
                                    log.debug("[Get GitHub Stars]Rate limit: {}", rateLimit);
                                    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                                        throw WebClientResponseException.create(
                                                responseEntity.getStatusCode().value(),
                                                responseEntity.getStatusCode().toString(),
                                                responseEntityHeaders,
                                                new byte[0],
                                                StandardCharsets.UTF_8
                                        );
                                    }
                                    List<StarsRepository> body = responseEntity.getBody();
                                    if (Objects.isNull(body) || body.isEmpty()) {
                                        log.warn("Github stars repository list is empty");
                                        return Flux.empty();
                                    }
                                    List<StarsRepository> filteredStarsRepository = body.stream()
                                            .filter(starsRepository -> {
                                                if (Objects.nonNull(gitHubStarsSummaryProperties.getIgnoreRepoPattern())) {
                                                    return gitHubStarsSummaryProperties.getIgnoreRepoPattern()
                                                            .asPredicate()
                                                            .negate()
                                                            .test(starsRepository.getFullName());
                                                }
                                                return true;
                                            })
                                            .toList();
                                    int remain = limit - counter.get();
                                    if (remain < filteredStarsRepository.size()) {
                                        filteredStarsRepository = filteredStarsRepository.subList(0, remain);
                                    }
                                    counter.accumulateAndGet(filteredStarsRepository.size(), Integer::sum);
                                    log.info("[Get GitHub Stars] Result Count: {}", filteredStarsRepository.size());
                                    return Flux.fromIterable(filteredStarsRepository)
                                            .concatWith(Flux.defer(() -> {
                                                List<String> linksUrl = responseEntityHeaders.get("link");
                                                if (Objects.isNull(linksUrl) || linksUrl.isEmpty()) {
                                                    log.warn("Links header was not found in response headers");
                                                    return Flux.empty();
                                                }
                                                Optional<String> firstNextLink = linksUrl.stream()
                                                        .flatMap(value -> Arrays.stream(StringUtils.split(value, ",")))
                                                        .map(String::trim)
                                                        .filter(link -> link.contains("rel=\"next\""))
                                                        .findFirst();
                                                if (firstNextLink.isEmpty()) {
                                                    log.warn(
                                                            "First rel=\"next\" header was not found in response headers, links:{}",
                                                            linksUrl
                                                    );
                                                    return Flux.empty();
                                                }
                                                String nextLink = firstNextLink.get();
                                                String actualLink = StringUtils.substringBetween(nextLink, "<", ">");
                                                log.debug("[Get GitHub Stars]Next Link: {}", actualLink);
                                                return Mono.defer(() -> Mono.just(actualLink))
                                                        .flatMapMany(link -> {
                                                            return Flux.defer(() -> {
                                                                return pagingStarsRepository(URI.create(link), limit);
                                                            });
                                                        });
                                            }));
                                });
                    });
        });
    }

    public Mono<ContentTree> getReadmeContent(URI uri) {
        URI readmeUri = UriComponentsBuilder.fromUri(uri)
                .path("/contents/README.md")
                .build()
                .toUri();
        return githubWebClient.get()
                .uri(readmeUri)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<ContentTree>() {
                })
                .flatMap(responseEntity -> {
                    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                        log.warn("Github readme content was not found in repo {}, response status: {}",
                                uri,
                                responseEntity.getStatusCode()
                        );
                        return Mono.empty();
                    }
                    log.info("[Get GitHub Readme]Readme uri: {}", readmeUri);
                    return Mono.justOrEmpty(responseEntity.getBody());
                });
    }

    public Mono<List<LanguageInfo>> getLanguageInfo(URI uri) {
        return githubWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<LinkedHashMap<String, Integer>>() {
                })
                .flatMapMany(dataMap -> {
                    log.info("[Get GitHub Language Info]LanguageInfo: {}", dataMap);
                    return Flux.fromIterable(dataMap.entrySet());}
                )
                .map(entry -> LanguageInfo.builder()
                        .language(entry.getKey())
                        .lines(entry.getValue())
                        .build()
                )
                .collectList();
    }

}
