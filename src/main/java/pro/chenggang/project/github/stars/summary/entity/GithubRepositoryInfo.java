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

    private final String description;

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
