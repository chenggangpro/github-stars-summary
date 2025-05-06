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
                .consumeNextWith(readmeContent -> {
                    byte[] decoded = Base64.getDecoder().decode(readmeContent.replace("\n", ""));
                    System.out.println("Content Tree:");
                    System.out.println(new String(decoded));
                })
                .verifyComplete();
    }
}