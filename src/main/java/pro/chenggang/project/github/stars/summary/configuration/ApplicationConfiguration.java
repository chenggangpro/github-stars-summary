package pro.chenggang.project.github.stars.summary.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import pro.chenggang.project.github.stars.summary.properties.GitHubStarsSummaryProperties;

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
    public WebClient webClient(WebClient.Builder builder, GitHubStarsSummaryProperties gitHubStarsSummaryProperties) {
        return builder.baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + gitHubStarsSummaryProperties.getGithubToken())
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }
}
