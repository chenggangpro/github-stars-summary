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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Repository
 * <p>
 * A repository on GitHub.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "node_id",
        "name",
        "full_name",
        "license",
        "forks",
        "permissions",
        "owner",
        "private",
        "html_url",
        "description",
        "fork",
        "url",
        "archive_url",
        "assignees_url",
        "blobs_url",
        "branches_url",
        "collaborators_url",
        "comments_url",
        "commits_url",
        "compare_url",
        "contents_url",
        "contributors_url",
        "deployments_url",
        "downloads_url",
        "events_url",
        "forks_url",
        "git_commits_url",
        "git_refs_url",
        "git_tags_url",
        "git_url",
        "issue_comment_url",
        "issue_events_url",
        "issues_url",
        "keys_url",
        "labels_url",
        "languages_url",
        "merges_url",
        "milestones_url",
        "notifications_url",
        "pulls_url",
        "releases_url",
        "ssh_url",
        "stargazers_url",
        "statuses_url",
        "subscribers_url",
        "subscription_url",
        "tags_url",
        "teams_url",
        "trees_url",
        "clone_url",
        "mirror_url",
        "hooks_url",
        "svn_url",
        "homepage",
        "language",
        "forks_count",
        "stargazers_count",
        "watchers_count",
        "size",
        "default_branch",
        "open_issues_count",
        "is_template",
        "topics",
        "has_issues",
        "has_projects",
        "has_wiki",
        "has_pages",
        "has_downloads",
        "has_discussions",
        "archived",
        "disabled",
        "visibility",
        "pushed_at",
        "created_at",
        "updated_at",
        "allow_rebase_merge",
        "temp_clone_token",
        "allow_squash_merge",
        "allow_auto_merge",
        "delete_branch_on_merge",
        "allow_update_branch",
        "use_squash_pr_title_as_default",
        "squash_merge_commit_title",
        "squash_merge_commit_message",
        "merge_commit_title",
        "merge_commit_message",
        "allow_merge_commit",
        "allow_forking",
        "web_commit_signoff_required",
        "open_issues",
        "watchers",
        "master_branch",
        "starred_at",
        "anonymous_access_enabled"
})
@Generated
@Jacksonized
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StarsRepository {

    /**
     * Unique identifier of the repository
     * (Required)
     */
    @JsonProperty("id")
    @JsonPropertyDescription("Unique identifier of the repository")
    private final Integer id;
    /**
     * (Required)
     */
    @JsonProperty("node_id")
    private final String nodeId;
    /**
     * The name of the repository.
     * (Required)
     */
    @JsonProperty("name")
    @JsonPropertyDescription("The name of the repository.")
    private final String name;
    /**
     * (Required)
     */
    @JsonProperty("full_name")
    private final String fullName;
    /**
     * (Required)
     */
    @JsonProperty("license")
    private final License license;
    /**
     * (Required)
     */
    @JsonProperty("forks")
    private final Integer forks;
    @JsonProperty("permissions")
    private final Permissions permissions;
    /**
     * Simple User
     * <p>
     * A GitHub user.
     * (Required)
     */
    @JsonProperty("owner")
    @JsonPropertyDescription("A GitHub user.")
    private final Owner owner;
    /**
     * Whether the repository is private or public.
     * (Required)
     */
    @JsonProperty("private")
    @JsonPropertyDescription("Whether the repository is private or public.")
    private final Boolean _private = false;
    /**
     * (Required)
     */
    @JsonProperty("html_url")
    private final URI htmlUrl;
    /**
     * (Required)
     */
    @JsonProperty("description")
    private final String description;
    /**
     * (Required)
     */
    @JsonProperty("fork")
    private final Boolean fork;
    /**
     * (Required)
     */
    @JsonProperty("url")
    private final URI url;
    /**
     * (Required)
     */
    @JsonProperty("archive_url")
    private final String archiveUrl;
    /**
     * (Required)
     */
    @JsonProperty("assignees_url")
    private final String assigneesUrl;
    /**
     * (Required)
     */
    @JsonProperty("blobs_url")
    private final String blobsUrl;
    /**
     * (Required)
     */
    @JsonProperty("branches_url")
    private final String branchesUrl;
    /**
     * (Required)
     */
    @JsonProperty("collaborators_url")
    private final String collaboratorsUrl;
    /**
     * (Required)
     */
    @JsonProperty("comments_url")
    private final String commentsUrl;
    /**
     * (Required)
     */
    @JsonProperty("commits_url")
    private final String commitsUrl;
    /**
     * (Required)
     */
    @JsonProperty("compare_url")
    private final String compareUrl;
    /**
     * (Required)
     */
    @JsonProperty("contents_url")
    private final String contentsUrl;
    /**
     * (Required)
     */
    @JsonProperty("contributors_url")
    private final URI contributorsUrl;
    /**
     * (Required)
     */
    @JsonProperty("deployments_url")
    private final URI deploymentsUrl;
    /**
     * (Required)
     */
    @JsonProperty("downloads_url")
    private final URI downloadsUrl;
    /**
     * (Required)
     */
    @JsonProperty("events_url")
    private final URI eventsUrl;
    /**
     * (Required)
     */
    @JsonProperty("forks_url")
    private final URI forksUrl;
    /**
     * (Required)
     */
    @JsonProperty("git_commits_url")
    private final String gitCommitsUrl;
    /**
     * (Required)
     */
    @JsonProperty("git_refs_url")
    private final String gitRefsUrl;
    /**
     * (Required)
     */
    @JsonProperty("git_tags_url")
    private final String gitTagsUrl;
    /**
     * (Required)
     */
    @JsonProperty("git_url")
    private final String gitUrl;
    /**
     * (Required)
     */
    @JsonProperty("issue_comment_url")
    private final String issueCommentUrl;
    /**
     * (Required)
     */
    @JsonProperty("issue_events_url")
    private final String issueEventsUrl;
    /**
     * (Required)
     */
    @JsonProperty("issues_url")
    private final String issuesUrl;
    /**
     * (Required)
     */
    @JsonProperty("keys_url")
    private final String keysUrl;
    /**
     * (Required)
     */
    @JsonProperty("labels_url")
    private final String labelsUrl;
    /**
     * (Required)
     */
    @JsonProperty("languages_url")
    private final URI languagesUrl;
    /**
     * (Required)
     */
    @JsonProperty("merges_url")
    private final URI mergesUrl;
    /**
     * (Required)
     */
    @JsonProperty("milestones_url")
    private final String milestonesUrl;
    /**
     * (Required)
     */
    @JsonProperty("notifications_url")
    private final String notificationsUrl;
    /**
     * (Required)
     */
    @JsonProperty("pulls_url")
    private final String pullsUrl;
    /**
     * (Required)
     */
    @JsonProperty("releases_url")
    private final String releasesUrl;
    /**
     * (Required)
     */
    @JsonProperty("ssh_url")
    private final String sshUrl;
    /**
     * (Required)
     */
    @JsonProperty("stargazers_url")
    private final URI stargazersUrl;
    /**
     * (Required)
     */
    @JsonProperty("statuses_url")
    private final String statusesUrl;
    /**
     * (Required)
     */
    @JsonProperty("subscribers_url")
    private final URI subscribersUrl;
    /**
     * (Required)
     */
    @JsonProperty("subscription_url")
    private final URI subscriptionUrl;
    /**
     * (Required)
     */
    @JsonProperty("tags_url")
    private final URI tagsUrl;
    /**
     * (Required)
     */
    @JsonProperty("teams_url")
    private final URI teamsUrl;
    /**
     * (Required)
     */
    @JsonProperty("trees_url")
    private final String treesUrl;
    /**
     * (Required)
     */
    @JsonProperty("clone_url")
    private final String cloneUrl;
    /**
     * (Required)
     */
    @JsonProperty("mirror_url")
    private final URI mirrorUrl;
    /**
     * (Required)
     */
    @JsonProperty("hooks_url")
    private final URI hooksUrl;
    /**
     * (Required)
     */
    @JsonProperty("svn_url")
    private final URI svnUrl;
    /**
     * (Required)
     */
    @JsonProperty("homepage")
    private final URI homepage;
    /**
     * (Required)
     */
    @JsonProperty("language")
    private final String language;
    /**
     * (Required)
     */
    @JsonProperty("forks_count")
    private final Integer forksCount;
    /**
     * (Required)
     */
    @JsonProperty("stargazers_count")
    private final Integer stargazersCount;
    /**
     * (Required)
     */
    @JsonProperty("watchers_count")
    private final Integer watchersCount;
    /**
     * The size of the repository, in kilobytes. Size is calculated hourly. When a repository is initially created, the size is 0.
     * (Required)
     */
    @JsonProperty("size")
    @JsonPropertyDescription("The size of the repository, in kilobytes. Size is calculated hourly. When a repository is initially created, the size is 0.")
    private final Integer size;
    /**
     * The default branch of the repository.
     * (Required)
     */
    @JsonProperty("default_branch")
    @JsonPropertyDescription("The default branch of the repository.")
    private final String defaultBranch;
    /**
     * (Required)
     */
    @JsonProperty("open_issues_count")
    private final Integer openIssuesCount;
    /**
     * Whether this repository acts as a template that can be used to generate new repositories.
     */
    @JsonProperty("is_template")
    @JsonPropertyDescription("Whether this repository acts as a template that can be used to generate new repositories.")
    private final Boolean isTemplate = false;
    @JsonProperty("topics")
    private final List<String> topics;
    /**
     * Whether issues are enabled.
     * (Required)
     */
    @JsonProperty("has_issues")
    @JsonPropertyDescription("Whether issues are enabled.")
    private final Boolean hasIssues = true;
    /**
     * Whether projects are enabled.
     * (Required)
     */
    @JsonProperty("has_projects")
    @JsonPropertyDescription("Whether projects are enabled.")
    private final Boolean hasProjects = true;
    /**
     * Whether the wiki is enabled.
     * (Required)
     */
    @JsonProperty("has_wiki")
    @JsonPropertyDescription("Whether the wiki is enabled.")
    private final Boolean hasWiki = true;
    /**
     * (Required)
     */
    @JsonProperty("has_pages")
    private final Boolean hasPages;
    /**
     * Whether downloads are enabled.
     * (Required)
     */
    @JsonProperty("has_downloads")
    @JsonPropertyDescription("Whether downloads are enabled.")
    private final Boolean hasDownloads = true;
    /**
     * Whether discussions are enabled.
     */
    @JsonProperty("has_discussions")
    @JsonPropertyDescription("Whether discussions are enabled.")
    private final Boolean hasDiscussions = false;
    /**
     * Whether the repository is archived.
     * (Required)
     */
    @JsonProperty("archived")
    @JsonPropertyDescription("Whether the repository is archived.")
    private final Boolean archived = false;
    /**
     * Returns whether or not this repository disabled.
     * (Required)
     */
    @JsonProperty("disabled")
    @JsonPropertyDescription("Returns whether or not this repository disabled.")
    private final Boolean disabled;
    /**
     * The repository visibility: public, private, or internal.
     */
    @JsonProperty("visibility")
    @JsonPropertyDescription("The repository visibility: public, private, or internal.")
    private final String visibility = "public";
    /**
     * (Required)
     */
    @JsonProperty("pushed_at")
    private final LocalDate pushedAt;
    /**
     * (Required)
     */
    @JsonProperty("created_at")
    private final LocalDate createdAt;
    /**
     * (Required)
     */
    @JsonProperty("updated_at")
    private final LocalDate updatedAt;
    /**
     * Whether to allow rebase merges for pull requests.
     */
    @JsonProperty("allow_rebase_merge")
    @JsonPropertyDescription("Whether to allow rebase merges for pull requests.")
    private final Boolean allowRebaseMerge = true;
    @JsonProperty("temp_clone_token")
    private final String tempCloneToken;
    /**
     * Whether to allow squash merges for pull requests.
     */
    @JsonProperty("allow_squash_merge")
    @JsonPropertyDescription("Whether to allow squash merges for pull requests.")
    private final Boolean allowSquashMerge = true;
    /**
     * Whether to allow Auto-merge to be used on pull requests.
     */
    @JsonProperty("allow_auto_merge")
    @JsonPropertyDescription("Whether to allow Auto-merge to be used on pull requests.")
    private final Boolean allowAutoMerge = false;
    /**
     * Whether to delete head branches when pull requests are merged
     */
    @JsonProperty("delete_branch_on_merge")
    @JsonPropertyDescription("Whether to delete head branches when pull requests are merged")
    private final Boolean deleteBranchOnMerge = false;
    /**
     * Whether or not a pull request head branch that is behind its base branch can always be updated even if it is not required to be up to date before merging.
     */
    @JsonProperty("allow_update_branch")
    @JsonPropertyDescription("Whether or not a pull request head branch that is behind its base branch can always be updated even if it is not required to be up to date before merging.")
    private final Boolean allowUpdateBranch = false;
    /**
     * Whether a squash merge commit can use the pull request title as default. **This property is closing down. Please use `squash_merge_commit_title` instead.
     */
    @JsonProperty("use_squash_pr_title_as_default")
    @JsonPropertyDescription("Whether a squash merge commit can use the pull request title as default. **This property is closing down. Please use `squash_merge_commit_title` instead.")
    private final Boolean useSquashPrTitleAsDefault = false;
    /**
     * The default value for a squash merge commit title:
     * <p>
     * - `PR_TITLE` - default to the pull request's title.
     * - `COMMIT_OR_PR_TITLE` - default to the commit's title (if only one commit) or the pull request's title (when more than one commit).
     */
    @JsonProperty("squash_merge_commit_title")
    @JsonPropertyDescription("The default value for a squash merge commit title:\n\n- `PR_TITLE` - default to the pull request's title.\n- `COMMIT_OR_PR_TITLE` - default to the commit's title (if only one commit) or the pull request's title (when more than one commit).")
    private final StarsRepository.SquashMergeCommitTitle squashMergeCommitTitle;
    /**
     * The default value for a squash merge commit message:
     * <p>
     * - `PR_BODY` - default to the pull request's body.
     * - `COMMIT_MESSAGES` - default to the branch's commit messages.
     * - `BLANK` - default to a blank commit message.
     */
    @JsonProperty("squash_merge_commit_message")
    @JsonPropertyDescription("The default value for a squash merge commit message:\n\n- `PR_BODY` - default to the pull request's body.\n- `COMMIT_MESSAGES` - default to the branch's commit messages.\n- `BLANK` - default to a blank commit message.")
    private final StarsRepository.SquashMergeCommitMessage squashMergeCommitMessage;
    /**
     * The default value for a merge commit title.
     * <p>
     * - `PR_TITLE` - default to the pull request's title.
     * - `MERGE_MESSAGE` - default to the classic title for a merge message (e.g., Merge pull request #123 from branch-name).
     */
    @JsonProperty("merge_commit_title")
    @JsonPropertyDescription("The default value for a merge commit title.\n\n- `PR_TITLE` - default to the pull request's title.\n- `MERGE_MESSAGE` - default to the classic title for a merge message (e.g., Merge pull request #123 from branch-name).")
    private final StarsRepository.MergeCommitTitle mergeCommitTitle;
    /**
     * The default value for a merge commit message.
     * <p>
     * - `PR_TITLE` - default to the pull request's title.
     * - `PR_BODY` - default to the pull request's body.
     * - `BLANK` - default to a blank commit message.
     */
    @JsonProperty("merge_commit_message")
    @JsonPropertyDescription("The default value for a merge commit message.\n\n- `PR_TITLE` - default to the pull request's title.\n- `PR_BODY` - default to the pull request's body.\n- `BLANK` - default to a blank commit message.")
    private final StarsRepository.MergeCommitMessage mergeCommitMessage;
    /**
     * Whether to allow merge commits for pull requests.
     */
    @JsonProperty("allow_merge_commit")
    @JsonPropertyDescription("Whether to allow merge commits for pull requests.")
    private final Boolean allowMergeCommit = true;
    /**
     * Whether to allow forking this repo
     */
    @JsonProperty("allow_forking")
    @JsonPropertyDescription("Whether to allow forking this repo")
    private final Boolean allowForking;
    /**
     * Whether to require contributors to sign off on web-based commits
     */
    @JsonProperty("web_commit_signoff_required")
    @JsonPropertyDescription("Whether to require contributors to sign off on web-based commits")
    private final Boolean webCommitSignoffRequired = false;
    /**
     * (Required)
     */
    @JsonProperty("open_issues")
    private final Integer openIssues;
    /**
     * (Required)
     */
    @JsonProperty("watchers")
    private final Integer watchers;
    @JsonProperty("master_branch")
    private final String masterBranch;
    @JsonProperty("starred_at")
    private final String starredAt;
    /**
     * Whether anonymous git access is enabled for this repository
     */
    @JsonProperty("anonymous_access_enabled")
    @JsonPropertyDescription("Whether anonymous git access is enabled for this repository")
    private final Boolean anonymousAccessEnabled;


    /**
     * Simple User
     * <p>
     * A GitHub user.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "name",
            "email",
            "login",
            "id",
            "node_id",
            "avatar_url",
            "gravatar_id",
            "url",
            "html_url",
            "followers_url",
            "following_url",
            "gists_url",
            "starred_url",
            "subscriptions_url",
            "organizations_url",
            "repos_url",
            "events_url",
            "received_events_url",
            "type",
            "site_admin",
            "starred_at",
            "user_view_type"
    })
    @Generated
    @Jacksonized
    @Builder
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Owner {

        @JsonProperty("name")
        private final String name;
        @JsonProperty("email")
        private final String email;
        /**
         * (Required)
         */
        @JsonProperty("login")
        private final String login;
        /**
         * (Required)
         */
        @JsonProperty("id")
        private final Integer id;
        /**
         * (Required)
         */
        @JsonProperty("node_id")
        private final String nodeId;
        /**
         * (Required)
         */
        @JsonProperty("avatar_url")
        private final URI avatarUrl;
        /**
         * (Required)
         */
        @JsonProperty("gravatar_id")
        private final String gravatarId;
        /**
         * (Required)
         */
        @JsonProperty("url")
        private final URI url;
        /**
         * (Required)
         */
        @JsonProperty("html_url")
        private final URI htmlUrl;
        /**
         * (Required)
         */
        @JsonProperty("followers_url")
        private final URI followersUrl;
        /**
         * (Required)
         */
        @JsonProperty("following_url")
        private final String followingUrl;
        /**
         * (Required)
         */
        @JsonProperty("gists_url")
        private final String gistsUrl;
        /**
         * (Required)
         */
        @JsonProperty("starred_url")
        private final String starredUrl;
        /**
         * (Required)
         */
        @JsonProperty("subscriptions_url")
        private final URI subscriptionsUrl;
        /**
         * (Required)
         */
        @JsonProperty("organizations_url")
        private final URI organizationsUrl;
        /**
         * (Required)
         */
        @JsonProperty("repos_url")
        private final URI reposUrl;
        /**
         * (Required)
         */
        @JsonProperty("events_url")
        private final String eventsUrl;
        /**
         * (Required)
         */
        @JsonProperty("received_events_url")
        private final URI receivedEventsUrl;
        /**
         * (Required)
         */
        @JsonProperty("type")
        private final String type;
        /**
         * (Required)
         */
        @JsonProperty("site_admin")
        private final Boolean siteAdmin;
        @JsonProperty("starred_at")
        private final String starredAt;
        @JsonProperty("user_view_type")
        private final String userViewType;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "admin",
            "pull",
            "triage",
            "push",
            "maintain"
    })
    @Generated
    @Jacksonized
    @Builder
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Permissions {

        /**
         * (Required)
         */
        @JsonProperty("admin")
        private final Boolean admin;
        /**
         * (Required)
         */
        @JsonProperty("pull")
        private final Boolean pull;
        @JsonProperty("triage")
        private final Boolean triage;
        /**
         * (Required)
         */
        @JsonProperty("push")
        private final Boolean push;
        @JsonProperty("maintain")
        private final Boolean maintain;

    }

    @Jacksonized
    @Builder
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class License {

        private final String name;

        private final String url;
    }

    /**
     * The default value for a merge commit message.
     * <p>
     * - `PR_TITLE` - default to the pull request's title.
     * - `PR_BODY` - default to the pull request's body.
     * - `BLANK` - default to a blank commit message.
     */
    @Generated
    public enum MergeCommitMessage {

        PR_BODY("PR_BODY"),
        PR_TITLE("PR_TITLE"),
        BLANK("BLANK");
        private final String value;
        private final static Map<String, StarsRepository.MergeCommitMessage> CONSTANTS = new HashMap<String, StarsRepository.MergeCommitMessage>();

        static {
            for (StarsRepository.MergeCommitMessage c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MergeCommitMessage(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static StarsRepository.MergeCommitMessage fromValue(String value) {
            StarsRepository.MergeCommitMessage constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The default value for a merge commit title.
     * <p>
     * - `PR_TITLE` - default to the pull request's title.
     * - `MERGE_MESSAGE` - default to the classic title for a merge message (e.g., Merge pull request #123 from branch-name).
     */
    @Generated
    public enum MergeCommitTitle {

        PR_TITLE("PR_TITLE"),
        MERGE_MESSAGE("MERGE_MESSAGE");
        private final String value;
        private final static Map<String, StarsRepository.MergeCommitTitle> CONSTANTS = new HashMap<String, StarsRepository.MergeCommitTitle>();

        static {
            for (StarsRepository.MergeCommitTitle c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MergeCommitTitle(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static StarsRepository.MergeCommitTitle fromValue(String value) {
            StarsRepository.MergeCommitTitle constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The default value for a squash merge commit message:
     * <p>
     * - `PR_BODY` - default to the pull request's body.
     * - `COMMIT_MESSAGES` - default to the branch's commit messages.
     * - `BLANK` - default to a blank commit message.
     */
    @Generated
    public enum SquashMergeCommitMessage {

        PR_BODY("PR_BODY"),
        COMMIT_MESSAGES("COMMIT_MESSAGES"),
        BLANK("BLANK");
        private final String value;
        private final static Map<String, StarsRepository.SquashMergeCommitMessage> CONSTANTS = new HashMap<String, StarsRepository.SquashMergeCommitMessage>();

        static {
            for (StarsRepository.SquashMergeCommitMessage c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SquashMergeCommitMessage(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static StarsRepository.SquashMergeCommitMessage fromValue(String value) {
            StarsRepository.SquashMergeCommitMessage constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    /**
     * The default value for a squash merge commit title:
     * <p>
     * - `PR_TITLE` - default to the pull request's title.
     * - `COMMIT_OR_PR_TITLE` - default to the commit's title (if only one commit) or the pull request's title (when more than one commit).
     */
    @Generated
    public enum SquashMergeCommitTitle {

        PR_TITLE("PR_TITLE"),
        COMMIT_OR_PR_TITLE("COMMIT_OR_PR_TITLE");
        private final String value;
        private final static Map<String, SquashMergeCommitTitle> CONSTANTS = new HashMap<String, SquashMergeCommitTitle>();

        static {
            for (StarsRepository.SquashMergeCommitTitle c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SquashMergeCommitTitle(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static StarsRepository.SquashMergeCommitTitle fromValue(String value) {
            StarsRepository.SquashMergeCommitTitle constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }
}