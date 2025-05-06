# [(${summary.repositoryName})]([(${summary.repositoryUrl})])
[# th:if="${summary.tags != null && !summary.tags.isEmpty()}"]
[# th:each="tag : ${summary.tags}"][(${tag})] [/]
[/]
[# th:if="${summary.license != null && !summary.license.isBlank()}"]**[(${summary.license})]**[/]

> [(${summary.shortDescription})]

[(${summary.summarizedContent.en})]

[(${summary.summarizedContent.zh})]

----
