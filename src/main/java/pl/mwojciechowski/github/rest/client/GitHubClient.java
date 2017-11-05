package pl.mwojciechowski.github.rest.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author mwojciechowski
 */
@FeignClient(name = "github-client", url = "${github.api.url:https://api.github.com/}")
public interface GitHubClient {

    @GetMapping("/repos/{owner}/{repositoryName}")
    GitHubRepositorySummary findRepositoryBy(@PathVariable("owner") String owner, @PathVariable("repositoryName") String repositoryName);
}
