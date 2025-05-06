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