### GitHub repository information
* The simple name of this github repository is `[(${repository.name})]`

* The full name of this github repository is `[(${repository.fullName})]`

* The url of this github repository is `[(${repository.url})]`
[# th:if="${repository.license != null}"]
* The license of this github repository is `[(${repository.license})]` and the license url is `[(${repository.licenseUrl})]`
[/]
[# th:if="${repository.languages != null && !repository.languages.isEmpty()}"]
* The usages of programming language in this github repository is
| language |lines|
|:---------|:----|
[# th:each="p : ${repository.languages}"]|[(${p.language})]|[(${p.lines})]|[/]
[/]
* The README file's content of github repository is

```text
[(${repository.readmeContent})]
```