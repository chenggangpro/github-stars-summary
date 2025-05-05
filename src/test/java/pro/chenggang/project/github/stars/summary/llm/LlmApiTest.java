package pro.chenggang.project.github.stars.summary.llm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.chenggang.project.github.stars.summary.GithubStarsSummaryApplicationTests;
import pro.chenggang.project.github.stars.summary.entity.GithubRepositoryInfo;
import pro.chenggang.project.github.stars.summary.github.GitHubApi;
import reactor.test.StepVerifier;

import java.util.Base64;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
class LlmApiTest extends GithubStarsSummaryApplicationTests {

    @Autowired
    GitHubApi githubApi;

    @Autowired
    LlmApi llmApi;

    @Test
    void summary() {
        githubApi.listStars(1)
                .next()
                .flatMap(starsRepository -> {
                    return githubApi.getReadmeContent(starsRepository.getUrl())
                            .map(contentTree -> {
                                byte[] decoded = Base64.getDecoder().decode(contentTree.getContent().replace("\n", ""));
                                return new String(decoded);
                            })
                            .flatMap(readmeContent -> {
                                GithubRepositoryInfo repositoryInfo = GithubRepositoryInfo.builder()
                                        .name(starsRepository.getName())
                                        .fullName(starsRepository.getFullName())
                                        .description(readmeContent)
                                        .readmeContent(readmeContent)
                                        .url(starsRepository.getHtmlUrl().toString())
                                        .build();
                                return llmApi.summary(repositoryInfo);
                            });
                })
                .as(StepVerifier::create)
                .consumeNextWith(result -> {
                    System.out.println(result);
                })
                .verifyComplete();
    }
}