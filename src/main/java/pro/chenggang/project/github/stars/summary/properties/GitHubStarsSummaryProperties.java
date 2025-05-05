package pro.chenggang.project.github.stars.summary.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Gang Cheng
 * @version 0.1.0
 * @since 0.1.0
 */
@Slf4j
@Getter
@Setter
public class GitHubStarsSummaryProperties implements InitializingBean {

    public static final String PREFIX = "github-stars-summary";

    /**
     * The github token for accessing github api
     * <p>
     * The fine-grained token must have the following permission set:
     * "Starring" user permissions (read)
     *
     * @see <a href="https://docs.github.com/en/rest/activity/starring?apiVersion=2022-11-28#list-repositories-starred-by-the-authenticated-user" />
     */
    private String githubToken;

    /**
     * The optional regex pattern of repository name which need to be ignored
     */
    private Pattern ignoreRepoPattern;

    /**
     * The llm api url
     */
    private URI llmUrl;

    /**
     * The llm api key (Optional)
     */
    private String llmApiKey;

    /**
     * The llm model name
     */
    private String llmModel;

    /**
     * The llm temperature
     */
    private Double temperature;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isBlank(githubToken)) {
            throw new IllegalArgumentException("Missing github token in github-stars-summary.yaml");
        }
        if(Objects.isNull(llmUrl) || StringUtils.isBlank(llmUrl.getHost()) || StringUtils.isBlank(llmUrl.getScheme())) {
            throw new IllegalArgumentException("Illegal llm url in file github-stars-summary.yaml");
        }
        if (StringUtils.isBlank(llmModel)) {
            throw new IllegalArgumentException("Missing llm model in github-stars-summary.yaml");
        }
        if (Objects.nonNull(ignoreRepoPattern.pattern()) && ignoreRepoPattern.pattern().isBlank()) {
            log.info("The ignore repo pattern is blank, force the pattern to be null");
            this.ignoreRepoPattern = null;
        }
    }
}
