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
package pro.chenggang.project.github.stars.summary.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.chenggang.project.github.stars.summary.GithubStarsSummaryApplicationTests;
import reactor.test.StepVerifier;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
class GithubStarsSummaryEntrypointTest extends GithubStarsSummaryApplicationTests {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    GithubStarsSummaryEntrypoint githubStarsSummaryEntrypoint;

    @Test
    void summary() {
        githubStarsSummaryEntrypoint.summary(-1, 5)
                .as(StepVerifier::create)
                .verifyComplete();
    }
}