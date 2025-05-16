package pro.chenggang.project.github.stars.summary.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Tags {
    Application(
            tags -> tags.stream()
                    .anyMatch(tag -> equalsAnyIgnoreCase(tag, "application"))
            ,
            "/documents/0.Application.md"
    ),
    JavaLanguage(
            tags -> {
                if (tags.size() == 1 && (tags.contains("java") || tags.contains("Java"))) {
                    return true;
                }
                boolean onlyJavaFramework = tags.stream()
                        .allMatch(tag -> equalsAnyIgnoreCase(tag, "java", "toolkit", "software", "Framework"));
                if (onlyJavaFramework) {
                    return true;
                }
                boolean anyJava = tags.stream().anyMatch(tag -> equalsAnyIgnoreCase(tag, "java", "JavaLanguage"));
                boolean anyLibrary = tags.stream()
                        .anyMatch(tag -> equalsAnyIgnoreCase(tag, "dependency", "dependencies", "library", "framework"));
                return anyJava && anyLibrary;
            },
            "/documents/1.JavaLanguage.md"
    ),
    RustLanguage(
            tags -> {
                boolean onlyRust = tags.stream()
                        .allMatch(tag -> equalsAnyIgnoreCase(tag, "rust", "toolkit", "software", "Framework"));
                if (onlyRust) {
                    return true;
                }
                boolean anyRust = tags.stream().anyMatch(tag -> equalsAnyIgnoreCase(tag, "rust", "RustLanguage"));
                boolean anyLibrary = tags.stream()
                        .anyMatch(tag -> equalsAnyIgnoreCase(tag, "dependency", "dependencies", "library"));
                return anyRust && anyLibrary;
            },
            "/documents/2.RustLanguage.md"
    ),
    AI_LLM(
            tags -> {
                return tags.stream().anyMatch(tag -> equalsAnyIgnoreCase(tag, "ai", "llm", "ai-llm"));
            },
            "/documents/4.AI-LLM.md"
    ),
    MachineLearning(
            tags -> {
                return tags.stream().anyMatch(tag -> equalsAnyIgnoreCase(tag, "machinelearning"));
            },
            "/documents/3.MachineLearning.md"
    ),
    WebDevelopment(
            tags -> {
                boolean anyWeb = tags.stream()
                        .anyMatch(tag -> equalsAnyIgnoreCase(tag,
                                "web",
                                "webdevelopment",
                                "ui",
                                "dependency",
                                "library",
                                "framework"
                        ));
                boolean noneSoftware = tags.stream().noneMatch(tag -> equalsAnyIgnoreCase(tag, "software"));
                return anyWeb && noneSoftware;
            },
            "/documents/5.WebRelated.md"
    ),
    MobileDevelopment(
            tags -> {
                boolean onlyMobile = tags.stream()
                        .allMatch(tag -> equalsAnyIgnoreCase(tag, "mobile"));
                if (onlyMobile) {
                    return true;
                }
                boolean onlyMobileSoftware = tags.stream().allMatch(tag -> equalsAnyIgnoreCase(tag, "mobile", "software"));
                if (onlyMobileSoftware) {
                    return false;
                }
                boolean matchMobile = tags.stream()
                        .allMatch(tag -> equalsAnyIgnoreCase(tag,
                                "mobile",
                                "flutter",
                                "ios",
                                "android",
                                "software",
                                "toolkit",
                                "Swift",
                                "reactnative"
                        ));
                if (matchMobile) {
                    return true;
                }
                boolean anyMobile = tags.stream()
                        .anyMatch(tag -> equalsAnyIgnoreCase(tag,
                                "mobile",
                                "mobiledevelopment",
                                "flutter",
                                "ios",
                                "android",
                                "Swift",
                                "reactnative",
                                "dependency",
                                "library"
                        ));
                boolean noneSoftware = tags.stream()
                        .noneMatch(tag -> equalsAnyIgnoreCase(tag, "software"));
                return anyMobile && noneSoftware;
            },
            "/documents/6.MobileRelated.md"
    ),
    DataProcessing(
            tags -> tags.stream()
                    .anyMatch(tag -> equalsAnyIgnoreCase(tag, "bigData", "data", "dataprocessing")),
            "/documents/9.DataProcessing.md"
    ),
    Database_SQL(
            tags -> tags.stream()
                    .anyMatch(tag -> equalsAnyIgnoreCase(tag, "database", "sql", "database-sql")),
            "/documents/8.Database-SQL.md"
    ),
    Documentation(
            tags -> tags.stream()
                    .anyMatch(tag -> equalsAnyIgnoreCase(tag, "Documentation", "article", "articles")),
            "/documents/7.Documentation.md"
    ),
    Course(
            tags -> tags.stream()
                    .anyMatch(tag -> equalsAnyIgnoreCase(tag, "course", "courses", "leaning", "book", "books")),
            "/documents/10.Course.md"
    ),
    Software(
            tags -> tags.stream()
                    .anyMatch(tag -> equalsAnyIgnoreCase(tag, "software")),
            "/documents/12.Software.md"
    ),
    Toolkit(
            tags -> tags.stream()
                    .anyMatch(tag -> equalsAnyIgnoreCase(tag, "toolkit")),
            "/documents/11.Toolkit.md"
    ),
    Others(tags -> true, "/documents/13.Others.md"),

    ;

    private final Predicate<Collection<String>> tagPredicate;
    private final String path;
}