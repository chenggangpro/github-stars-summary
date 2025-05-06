package pro.chenggang.project.github.stars.summary.llm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pro.chenggang.project.github.stars.summary.entity.GithubRepositoryInfo;
import pro.chenggang.project.github.stars.summary.llm.dto.LlmChatResponse;
import pro.chenggang.project.github.stars.summary.llm.dto.LlmChatResponse.Choice;
import pro.chenggang.project.github.stars.summary.llm.dto.LlmChatResponse.Message;
import pro.chenggang.project.github.stars.summary.llm.dto.LlmRequest;
import pro.chenggang.project.github.stars.summary.llm.dto.LlmRequest.JsonSchema;
import pro.chenggang.project.github.stars.summary.llm.dto.LlmRequest.LlmMessage;
import pro.chenggang.project.github.stars.summary.llm.dto.LlmRequest.LlmRequestBuilder;
import pro.chenggang.project.github.stars.summary.llm.dto.LlmRequest.ResponseFormat;
import pro.chenggang.project.github.stars.summary.llm.dto.SummaryResponse;
import pro.chenggang.project.github.stars.summary.properties.GitHubStarsSummaryProperties;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LlmApi {

    @Qualifier("llmWebClient")
    private final WebClient llmWebClient;
    private final ObjectMapper objectMapper;
    private final TemplateEngine templateEngine;
    private final GitHubStarsSummaryProperties gitHubStarsSummaryProperties;

    public Mono<SummaryResponse> summary(GithubRepositoryInfo githubRepositoryInfo) {
        return Mono.fromCallable(() -> {
                    SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12,
                            OptionPreset.PLAIN_JSON
                    )
                            .with(Option.FORBIDDEN_ADDITIONAL_PROPERTIES_BY_DEFAULT)
                            .withObjectMapper(objectMapper)
                            .with(new JacksonModule(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED));
                    SchemaGeneratorConfig config = configBuilder.build();
                    SchemaGenerator generator = new SchemaGenerator(config);
                    return (JsonNode) generator.generateSchema(SummaryResponse.class);
                })
                .flatMap(jsonSchema -> {
                    LlmRequestBuilder llmRequestBuilder = LlmRequest.builder()
                            .model(gitHubStarsSummaryProperties.getLlmModel());
                    if (Objects.nonNull(gitHubStarsSummaryProperties.getTemperature())) {
                        llmRequestBuilder.temperature(gitHubStarsSummaryProperties.getTemperature());
                    }
                    LlmRequest llmRequest = llmRequestBuilder.responseFormat(ResponseFormat.builder()
                                    .jsonSchema(JsonSchema.builder()
                                            .schema(jsonSchema)
                                            .build()
                                    )
                                    .build()
                            )
                            .messages(this.getMessages(githubRepositoryInfo, jsonSchema.toString()))
                            .build();
                    try {
                        log.trace("[LLM Summary]LlmRequest: \n {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(llmRequest));
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }
                    return llmWebClient.post()
                            .uri(gitHubStarsSummaryProperties.getLlmUrl())
                            .bodyValue(llmRequest)
                            .retrieve()
                            .bodyToMono(LlmChatResponse.class)
                            .flatMap(llmChatResponse -> {
                                List<Choice> choices = llmChatResponse.getChoices();
                                if (Objects.isNull(choices) || choices.isEmpty()) {
                                    return Mono.error(new IllegalStateException("LLm chat response is empty"));
                                }
                                Choice choice = choices.get(0);
                                Message message = choice.getMessage();
                                if (Objects.isNull(message)) {
                                    return Mono.error(new IllegalStateException("The message of lLm chat response is empty"));
                                }
                                String content = message.getContent();
                                if (StringUtils.isBlank(content)) {
                                    return Mono.error(new IllegalStateException("The content of lLm chat response is empty"));
                                }
                                return Mono.just(content)
                                        .map(contentValue -> {
                                            if (contentValue.startsWith("<think>")) {
                                                log.debug("[LLM Summary]Response is thinking result, remove thinking content");
                                                contentValue = StringUtils.trim(StringUtils.substringAfter(
                                                        contentValue,
                                                        "</think>"
                                                ));
                                            } else {
                                                contentValue = StringUtils.trim(contentValue);
                                            }
                                            if (contentValue.startsWith("<answer>")) {
                                                log.debug("[LLM Summary]Response has answer label, remove answer label");
                                                contentValue = StringUtils.trim(StringUtils.substringBetween(
                                                        contentValue,
                                                        "<answer>",
                                                        "</answer>"
                                                ));
                                            }
                                            return contentValue;
                                        })
                                        .flatMap(contentValue -> {
                                            try {
                                                log.trace("[LLM Summary]Response Content:\n {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contentValue));
                                            } catch (JsonProcessingException e) {
                                                return Mono.error(e);
                                            }
                                            return Mono.fromCallable(() -> {
                                                return objectMapper.readValue(contentValue, SummaryResponse.class);
                                            });
                                        })
                                        .switchIfEmpty(Mono.error(new IllegalStateException(
                                                "The summary response is empty")));
                            })
                            .onErrorResume(WebClientResponseException.class, throwable -> {
                                        System.out.println(throwable.getResponseBodyAsString());
                                        return Mono.error(throwable);
                                    }
                            );
                });
    }

    private List<LlmMessage> getMessages(GithubRepositoryInfo githubRepositoryInfo, String jsonSchema) {
        return List.of(this.getSystemPrompt(jsonSchema), this.getUserPrompt(githubRepositoryInfo));
    }

    private LlmMessage getSystemPrompt(String outputJsonSchema) {
        Context context = new Context();
        context.setVariable("jsonSchema", outputJsonSchema);
        String systemPrompt = templateEngine.process("system-prompt", context);
        return LlmMessage.builder()
                .role("system")
                .content(systemPrompt)
                .build();
    }

    private LlmMessage getUserPrompt(GithubRepositoryInfo githubRepositoryInfo) {
        Context context = new Context();
        context.setVariable("repository", githubRepositoryInfo);
        String userPrompt = templateEngine.process("user-prompt", context);
        return LlmMessage.builder()
                .role("user")
                .content(userPrompt)
                .build();
    }
}
