package pro.chenggang.project.github.stars.summary.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import pro.chenggang.project.github.stars.summary.properties.GitHubStarsSummaryProperties;

import java.net.URI;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

    @Bean
    @ConfigurationProperties(prefix = GitHubStarsSummaryProperties.PREFIX)
    public GitHubStarsSummaryProperties gitHubStarsSummaryProperties() {
        return new GitHubStarsSummaryProperties();
    }

    @Bean
    public WebClient githubWebClient(WebClient.Builder builder, GitHubStarsSummaryProperties gitHubStarsSummaryProperties) {
        return builder.baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + gitHubStarsSummaryProperties.getGithubToken())
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    @Bean
    public WebClient llmWebClient(WebClient.Builder builder, GitHubStarsSummaryProperties gitHubStarsSummaryProperties) {
        URI llmUrl = gitHubStarsSummaryProperties.getLlmUrl();
        UriComponentsBuilder componentsBuilder = UriComponentsBuilder.newInstance()
                .scheme(llmUrl.getScheme())
                .host(llmUrl.getHost());
        if (llmUrl.getPort() > 0) {
            componentsBuilder.port(llmUrl.getPort());
        }
        builder.baseUrl(componentsBuilder.build().toString())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        if (StringUtils.isNotBlank(gitHubStarsSummaryProperties.getLlmApiKey())) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + gitHubStarsSummaryProperties.getLlmApiKey());
        }
        return builder.build();
    }
}
