package pro.chenggang.project.github.stars.summary.github.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "encoding",
        "size",
        "name",
        "path",
        "content",
        "sha",
        "url",
        "git_url",
        "html_url",
        "download_url",
        "_links",
        "target",
        "submodule_git_url"
})
@Jacksonized
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Generated
public class ReadmeContent {

    /**
     * (Required)
     */
    @JsonProperty("type")
    private final ReadmeContent.Type type;
    /**
     * (Required)
     */
    @JsonProperty("encoding")
    private final String encoding;
    /**
     * (Required)
     */
    @JsonProperty("size")
    private final Integer size;
    /**
     * (Required)
     */
    @JsonProperty("name")
    private final String name;
    /**
     * (Required)
     */
    @JsonProperty("path")
    private final String path;
    /**
     * (Required)
     */
    @JsonProperty("content")
    private final String content;
    /**
     * (Required)
     */
    @JsonProperty("sha")
    private final String sha;
    /**
     * (Required)
     */
    @JsonProperty("url")
    private final URI url;
    /**
     * (Required)
     */
    @JsonProperty("git_url")
    private final URI gitUrl;
    /**
     * (Required)
     */
    @JsonProperty("html_url")
    private final URI htmlUrl;
    /**
     * (Required)
     */
    @JsonProperty("download_url")
    private final URI downloadUrl;
    /**
     * (Required)
     */
    @JsonProperty("_links")
    private final Links links;
    @JsonProperty("target")
    private final String target;
    @JsonProperty("submodule_git_url")
    private final String submoduleGitUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "git",
            "html",
            "self"
    })
    @Jacksonized
    @Builder
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Generated
    public static class Links {

        /**
         * (Required)
         */
        @JsonProperty("git")
        private final URI git;
        /**
         * (Required)
         */
        @JsonProperty("html")
        private final URI html;
        /**
         * (Required)
         */
        @JsonProperty("self")
        private final URI self;

    }

    @Generated
    public enum Type {

        FILE("file");
        private final String value;
        private final static Map<String, Type> CONSTANTS = new HashMap<String, ReadmeContent.Type>();

        static {
            for (ReadmeContent.Type c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static ReadmeContent.Type fromValue(String value) {
            ReadmeContent.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
