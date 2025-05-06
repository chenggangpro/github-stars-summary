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
package pro.chenggang.project.github.stars.summary.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@Jacksonized
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GithubRepositoryInfo {

    private final String name;

    private final String fullName;

    private final String url;

    private final String license;

    private final String licenseUrl;

    private final String readmeContent;

    private final List<LanguageInfo> languages;

    @Jacksonized
    @Builder
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class LanguageInfo {

        private final String language;

        private final Integer lines;
    }

}
