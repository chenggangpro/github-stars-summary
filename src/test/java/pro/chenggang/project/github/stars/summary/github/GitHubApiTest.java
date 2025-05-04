package pro.chenggang.project.github.stars.summary.github;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.chenggang.project.github.stars.summary.GithubStarsSummaryApplicationTests;
import reactor.test.StepVerifier;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
class GitHubApiTest extends GithubStarsSummaryApplicationTests {

    @Autowired
    GitHubApi githubApi;

    @Test
    void listStars() {
        githubApi.listStars()
                .take(20)
                .collectList()
                .as(StepVerifier::create)
                .consumeNextWith(stars -> {
                    stars.forEach(starsRepository -> System.out.println("Stars Repository Id: " + starsRepository.getId()));
                })
                .verifyComplete();
    }
}