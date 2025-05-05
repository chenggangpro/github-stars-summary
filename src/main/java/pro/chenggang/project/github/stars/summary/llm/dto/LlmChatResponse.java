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