package pro.chenggang.project.github.stars.summary.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import static pro.chenggang.project.github.stars.summary.GithubStarsSummaryApplication.GITHUB_TOKEN;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + GITHUB_TOKEN)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }
}
