package pro.chenggang.project.github.stars.summary.github;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.chenggang.project.github.stars.summary.GithubStarsSummaryApplicationTests;
import reactor.test.StepVerifier;

import java.util.Base64;

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
        githubApi.listStars(2)
                .collectList()
                .as(StepVerifier::create)
                .consumeNextWith(stars -> {
                    System.out.println(stars.size());
                })
                .verifyComplete();
    }

    @Test
    void getReadmeContent() {
        githubApi.listStars(5)
                .next()
                .flatMap(starsRepository -> githubApi.getReadmeContent(starsRepository.getUrl()))
                .as(StepVerifier::create)
                .consumeNextWith(contentTree -> {
                    byte[] decoded = Base64.getDecoder().decode(contentTree.getContent().replace("\n", ""));
                    System.out.println("Content Tree:");
                    System.out.println(new String(decoded));
                })
                .verifyComplete();
    }
}