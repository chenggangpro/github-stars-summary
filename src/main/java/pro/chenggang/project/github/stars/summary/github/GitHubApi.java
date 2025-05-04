package pro.chenggang.project.github.stars.summary.github;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import pro.chenggang.project.github.stars.summary.github.dto.ContentTree;
import pro.chenggang.project.github.stars.summary.github.dto.StarsRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubApi {

    private final WebClient webClient;

    /**
     * curl -L \
     * -H "Accept: application/vnd.github+json" \
     * -H "Authorization: Bearer <YOUR-TOKEN>" \
     * -H "X-GitHub-Api-Version: 2022-11-28" \
     * https://api.github.com/user/starred
     */
    public Flux<StarsRepository> listStars() {
        return this.pagingStarsRepository(URI.create("/user/starred?per_page=10&direction=asc"));
    }

    private Flux<StarsRepository> pagingStarsRepository(URI uri) {
        String scheme = uri.getScheme();
        RequestHeadersSpec<?> requestHeadersSpec;
        if (StringUtils.isNotBlank(scheme)) {
            requestHeadersSpec = webClient.get()
                    .uri(uri);
        } else {
            requestHeadersSpec = webClient.get()
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
                    return Flux.fromIterable(body)
                            .concatWith(Flux.defer(() -> {
                                HttpHeaders headers = responseEntity.getHeaders();
                                List<String> linksUrl = headers.get("link");
                                if (Objects.isNull(linksUrl) || linksUrl.isEmpty()) {
                                    log.warn("Links header was not found in response headers");
                                    return Flux.empty();
                                }
                                Optional<String> firstNextLink = linksUrl.stream()
                                        .filter(link -> link.contains("rel=\"next\""))
                                        .findFirst();
                                if (firstNextLink.isEmpty()) {
                                    log.warn("First rel=\"next\" header was not found in response headers, links:{}", linksUrl);
                                    return Flux.empty();
                                }
                                String nextLink = firstNextLink.get();
                                String actualLink = StringUtils.substringBetween(nextLink, "<", ">");
                                return pagingStarsRepository(URI.create(actualLink));
                            }));
                });
    }

    public Mono<ContentTree> getReadmeContent(URI uri) {
        return webClient.get()
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
                        log.warn("Github readme content was not found in repo {}", uri);
                        return Mono.empty();
                    }
                    return Mono.justOrEmpty(responseEntity.getBody());
                });
    }

}
