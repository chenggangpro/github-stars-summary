package pro.chenggang.project.github.stars.summary.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

    @Bean
    public WebClient webClient(WebClient.Builder builder, Environment environment) {
        String githubToken = environment.getProperty("GITHUB-TOKEN");
        if (StringUtils.isBlank(githubToken)) {
            throw new IllegalArgumentException("Missing GITHUB-TOKEN in file github-token.properties");
        }
        return builder.baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubToken)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }
}
