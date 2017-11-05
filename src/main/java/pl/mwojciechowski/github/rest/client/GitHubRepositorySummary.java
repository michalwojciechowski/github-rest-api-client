package pl.mwojciechowski.github.rest.client;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a single GitHub repository along with basic attributes. Driven by the interview task specification.
 * Intentionally reused to cover both cases at the same time:
 * <ul>
 * <li>as an official response from the GitHub servers {@link GitHubClient#findRepositoryBy(String, String)}</li>
 * <li>and as a response from in-house {@link GithubRepositoryController#showRepositorySummary(String, String)}</li>
 * </ul>
 * For more complex cases I would use either:
 * <ul>
 * <li>2 independent & dedicated classes</li>
 * <li>dynamic mapping done at runtime through manual serialization with Jackson</li>
 * </ul>
 *
 * @author mwojciechowski
 */
@AllArgsConstructor
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class GitHubRepositorySummary {

    @JsonAlias("full_name")
    private String fullName;

    private String description;

    @JsonAlias("clone_url")
    private String cloneUrl;

    @JsonAlias("stargazers_count")
    private int stars;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonAlias("created_at")
    private LocalDateTime createdAt;
}
