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
package pro.chenggang.project.github.stars.summary.github.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.net.URI;
import java.util.List;

/**
 * Content Tree
 * <p>
 * Content Tree
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "size",
        "name",
        "path",
        "sha",
        "content",
        "url",
        "git_url",
        "html_url",
        "download_url",
        "entries",
        "encoding",
        "_links"
})
@Generated
@Jacksonized
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ContentTree {

    /**
     * (Required)
     */
    @JsonProperty("type")
    private final String type;
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
    @JsonProperty("sha")
    private final String sha;
    @JsonProperty("content")
    private final String content;
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
    @JsonProperty("entries")
    private final List<Entry> entries;
    @JsonProperty("encoding")
    private final String encoding;
    /**
     * (Required)
     */
    @JsonProperty("_links")
    private final Links links;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "type",
            "size",
            "name",
            "path",
            "sha",
            "url",
            "git_url",
            "html_url",
            "download_url",
            "_links"
    })
    @Generated
    @Jacksonized
    @Builder
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Entry {

        /**
         * (Required)
         */
        @JsonProperty("type")
        private final String type;
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

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "git",
            "html",
            "self"
    })
    @Generated
    @Jacksonized
    @Builder
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
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
}

