package pro.chenggang.project.github.stars.summary.llm.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_WRITE;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@JsonClassDescription("The summarization result of given github repository")
public class SummaryResponse {

    @JsonProperty(value = "repository_url", access = READ_WRITE, required = true)
    @JsonPropertyDescription("The access url of given github repository")
    private final String repositoryUrl;

    @JsonProperty(value = "repository_name", access = READ_WRITE, required = true)
    @JsonPropertyDescription("The simple name of given github repository")
    private final String repositoryName;

    @JsonProperty(value = "repository_full_name", access = READ_WRITE, required = true)
    @JsonPropertyDescription("The full name of given github repository")
    private final String repositoryFullName;

    @JsonProperty(value = "summarized_content", access = READ_WRITE, required = true)
    @JsonPropertyDescription("The localized summarized content of given github repository in english and chinese")
    private final LocaleData summarizedContent;

    @JsonProperty(value = "tags", access = READ_WRITE, required = true)
    @JsonPropertyDescription("The localized tag list of given github repository in english and chinese")
    private final List<LocaleData> tags;

    @JsonProperty(value = "repository_type", access = READ_WRITE, required = true)
    @JsonPropertyDescription("The localized type of given github repository in english and chinese")
    private final LocaleData repositoryType;

    @JsonProperty(value = "license", access = READ_WRITE)
    @JsonPropertyDescription("The license of given github repository")
    private final String license;

    @JsonProperty(value = "programming_languages", access = READ_WRITE)
    @JsonPropertyDescription("The programming languages of given github repository")
    private final List<String> programmingLanguages;

    @Getter
    @Builder
    @Jacksonized
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonClassDescription("The locale data in english and in chinese")
    public static class LocaleData {

        @JsonProperty(value = "en", access = READ_WRITE, required = true)
        @JsonPropertyDescription("The data content in english")
        private final String en;

        @JsonProperty(value = "zh", access = READ_WRITE, required = true)
        @JsonPropertyDescription("The data content in chinese")
        private final String zh;
    }
}
