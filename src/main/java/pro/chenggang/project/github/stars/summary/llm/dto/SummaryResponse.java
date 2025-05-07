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
@Builder(toBuilder = true)
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

    @JsonProperty(value = "short_description", access = READ_WRITE, required = true)
    @JsonPropertyDescription("The short description of given github repository in chinese only")
    private final String shortDescription;

    @JsonProperty(value = "summarized_content", access = READ_WRITE, required = true)
    private final SummarizedContent summarizedContent;

    @JsonProperty(value = "tags", access = READ_WRITE, required = true)
    @JsonPropertyDescription("The localized tag list of given github repository in english, the tags must be chosen from the list below: \n 1. **Web**: A system of interlinked documents and resources accessible via the internet, typically viewed through browsers.\n" +
            "   2. **UI**: User Interface, the visual and interactive elements through which users interact with software or devices.\n" +
            "   3. **Toolkit**: A collection of tools and libraries designed to facilitate software development or specific tasks.\n" +
            "   4. **WebComponent**: Reusable, encapsulated HTML elements for building modular web interfaces.\n" +
            "   5. **Java**: A versatile, object-oriented programming language used for cross-platform applications.\n" +
            "   6. **Python**: A high-level, easy-to-read programming language popular for web development, data analysis, and AI.\n" +
            "   7. **TypeScript**: A superset of JavaScript adding static typing, used for scalable web applications.\n" +
            "   8. **Software**: A computer software featuring an intuitive user interface that can be seamlessly operated in any operating systems, excluding embedded systems.\n" +
            "   9. **AI**: Artificial Intelligence, simulation of human intelligence processes by machines.\n" +
            "   10. **MachineLearning**: A subset of AI involving algorithms that improve automatically through data.\n" +
            "   11. **Mobile**: Portable devices like smartphones and tablets that run mobile apps.\n" +
            "   12. **IOS**: The operating system for Apple mobile devices.\n" +
            "   13. **Android**: An open-source OS for smartphones and tablets by Google.\n" +
            "   14. **Kotlin**: A modern language interoperable with Java, mainly for Android development.\n" +
            "   15. **Framework**: A platform or foundation for developing specific types of software applications.\n" +
            "   16. **Course**: A structured series of lessons or training to teach a skill or topic.\n" +
            "   17. **Rust**: A systems programming language focused on safety and performance.\n" +
            "   18. **Dependency**: External libraries or packages or starters that a project relies on.\n" +
            "   19. **Spring**: A Java framework for building enterprise applications.\n" +
            "   20. **SpringCloud**: An extension for developing cloud-native microservices with Spring.\n" +
            "   21. **LLM**: Large Language Model, AI models trained on extensive text data for language tasks.\n" +
            "   22. **Video**: Moving visual media used in entertainment or communication.\n" +
            "   23. **Orchestration**: Managing and coordinating complex workflows across multiple systems.\n" +
            "   24. **DevOps**: Practices combining development and IT operations for faster software delivery.\n" +
            "   25. **Audio**: Sound data used in multimedia content like music or speech.\n" +
            "   26. **MacOS**: Apple’s desktop operating system for Mac computers.\n" +
            "   27. **Cloudflare**: A web infrastructure company providing CDN, security, and performance services.\n" +
            "   28. **JapaneseLearning**: Resources or activities for learning the Japanese language.\n" +
            "   29. **EnglishLearning**: Resources or activities for improving English language skills.\n" +
            "   30. **Docker**: A platform for creating, deploying, and managing lightweight, portable containers.\n" +
            "   31. **RAG**: Retrieval-Augmented Generation, an AI technique combining data retrieval with generation.\n" +
            "   32. **Article**: A written piece, often informative or opinion-based.\n" +
            "   33. **Experience**: Practical knowledge gained through involvement or exposure over time.\n" +
            "   34. **OperatingSystem**: Software managing hardware and software resources (e.g., Windows, Linux).\n" +
            "   35. **EmbeddedProgramming**: Writing software for embedded systems like microcontrollers.\n" +
            "   36. **JavaScript**: A scripting language used for dynamic web content and client-side programming.\n" +
            "   37. **MySQL**: An open-source relational database management system.\n" +
            "   38. **Database**: An organized collection of data for storage and retrieval.\n" +
            "   39. **Blender**: An open-source 3D modeling and animation software.\n" +
            "   40. **Go**: Also known as Golang, a statically typed, compiled programming language designed for simplicity, performance, and concurrency.")
    private final List<String> tags;

    @JsonProperty(value = "license", access = READ_WRITE, required = true)
    @JsonPropertyDescription("The license of given github repository, if no explicit license found then set this be blank string")
    private final String license;

    @JsonProperty(value = "programming_languages", access = READ_WRITE, required = true)
    @JsonPropertyDescription("The programming languages of given github repository")
    private final List<String> programmingLanguages;

    @Getter
    @Builder
    @Jacksonized
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonClassDescription("The localized summarized content of given github repository in english and chinese. The summarization content should start with a full-description of given repository then add some key features output as a list. The content should be formatted in markdown style")
    public static class SummarizedContent {

        @JsonProperty(value = "en", access = READ_WRITE, required = true)
        @JsonPropertyDescription("The summarized content in english")
        private final String en;

        @JsonProperty(value = "zh", access = READ_WRITE, required = true)
        @JsonPropertyDescription("The summarized content in chinese")
        private final String zh;
    }
}
