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
     * https://api.github.com/user/starred
     */
    public Flux<StarsRepository> listStars(int limit) {
        int perPage = 30;
        if (limit > 0 && limit <= 30) {
            perPage = limit;
        }
        return this.pagingStarsRepository(URI.create("/user/starred?per_page=" + perPage + "&direction=asc"), limit)
                .contextWrite(context -> context.put(AtomicInteger.class, new AtomicInteger(0)));
    }

    private Flux<StarsRepository> pagingStarsRepository(URI uri, int limit) {
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
                    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                        throw WebClientResponseException.create(
                                responseEntity.getStatusCode().value(),
                                responseEntity.getStatusCode().toString(),
                                responseEntity.getHeaders(),
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
                    return Flux.deferContextual(contextView -> {
                        return Mono.just(contextView.get(AtomicInteger.class))
                                .flatMapMany(counter -> {
                                    if (limit > 0 && counter.get() >= limit) {
                                        return Flux.empty();
                                    }
                                    counter.accumulateAndGet(filteredStarsRepository.size(), Integer::sum);
                                    return Flux.fromIterable(filteredStarsRepository)
                                            .concatWith(Flux.defer(() -> {
                                                HttpHeaders headers = responseEntity.getHeaders();
                                                List<String> linksUrl = headers.get("link");
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
        return githubWebClient.get()
                .uri(UriComponentsBuilder.fromUri(uri)
                        .path("/contents/README.md")
                        .build()
                        .toUri()
                )
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
                    return Mono.justOrEmpty(responseEntity.getBody());
                });
    }

    public Mono<List<LanguageInfo>> getLanguageInfo(URI uri) {
        return githubWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<LinkedHashMap<String, Integer>>() {
                })
                .flatMapMany(dataMap -> Flux.fromIterable(dataMap.entrySet()))
                .map(entry -> LanguageInfo.builder()
                        .language(entry.getKey())
                        .lines(entry.getValue())
                        .build()
                )
                .collectList();
    }

}
