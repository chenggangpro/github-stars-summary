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
package pro.chenggang.project.github.stars.summary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pro.chenggang.project.github.stars.summary.entity.GithubRepositoryInfo;
import pro.chenggang.project.github.stars.summary.entity.GithubRepositoryInfo.LanguageInfo;

import java.util.List;

@SpringBootTest
public class GithubStarsSummaryApplicationTests {

    @Autowired
    TemplateEngine templateEngine;

    @Test
    void contextLoads() {
        GithubRepositoryInfo repositoryInfo = GithubRepositoryInfo.builder()
                .url("https://github.com/chenggang")
                .name("chenggang")
                .description("Chenggang")
                .readmeContent("README.md")
                .languages(List.of(
                        LanguageInfo.builder()
                                .language("java")
                                .lines(123)
                                .build(),
                        LanguageInfo.builder()
                                .language("python")
                                .lines(456)
                                .build()
                ))
                .license("MIT")
                .licenseUrl("https://github.com/chenggang/chenggang")
                .build();
        Context context = new Context();
        context.setVariable("repository",repositoryInfo);
        String processed = templateEngine.process("user-prompt", context);
        System.out.println(processed);
    }
}
