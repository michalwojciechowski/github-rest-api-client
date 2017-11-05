package pl.mwojciechowski.github.rest.client;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mwojciechowski
 */
@RestController
public class GithubRepositoryController {

    private final GitHubClient gitHubClient;

    public GithubRepositoryController(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @GetMapping("/repositories/{owner}/{repositoryName}")
    public GitHubRepositorySummary showRepositorySummary(@PathVariable String owner, @PathVariable String repositoryName) {
        return gitHubClient.findRepositoryBy(owner, repositoryName);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception exception) {
        return new ResponseEntity<>(ExceptionUtils.getStackTrace(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
