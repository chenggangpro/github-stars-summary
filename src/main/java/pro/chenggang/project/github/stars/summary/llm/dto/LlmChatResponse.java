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

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LlmChatResponse {

    private final String id;
    private final long created;
    private final List<Choice> choices;
    private final Usage usage;
    private final Map<String, Object> modelInfo;

    @Getter
    @Builder
    @Jacksonized
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Choice {

        private final int index;
        private final Message message;
        private final String finishReason;

    }

    @Getter
    @Builder
    @Jacksonized
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Message {

        private final String role;
        private final String content;
        private final Map<String, Object> toolCalls;

    }

    @Getter
    @Builder
    @Jacksonized
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Usage {

        private final int promptTokens;
        private final int completionTokens;
        private final int totalTokens;

    }
}