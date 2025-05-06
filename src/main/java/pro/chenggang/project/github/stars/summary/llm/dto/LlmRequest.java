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
package pro.chenggang.project.github.stars.summary.llm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
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
@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LlmRequest {

    private final String model;

    @Builder.Default
    private final double temperature = 0.7;

    @Builder.Default
    private final boolean stream = false;

    private final List<LlmMessage> messages;

    @JsonProperty("response_format")
    private final ResponseFormat responseFormat;

    @Getter
    @Builder
    @Jacksonized
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LlmMessage {

        private final String role;

        private final String content;
    }

    @Getter
    @Builder
    @Jacksonized
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ResponseFormat {

        @Builder.Default
        private final String type = "json_schema";

        @JsonProperty("json_schema")
        private final JsonSchema jsonSchema;
    }

    /**
     * JSON schema object that describes the format of the JSON object. Applicable for the
     * 'json_schema' type only.
     */
    @Getter
    @Builder
    @Jacksonized
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonInclude(Include.NON_NULL)
    public static class JsonSchema {

        @JsonProperty("name")
        @Builder.Default
        private final String name = "SummaryResponse";

        @JsonProperty("schema")
        private final JsonNode schema;

        @JsonProperty("strict")
        @Builder.Default
        private final Boolean strict = true;


    }
}
