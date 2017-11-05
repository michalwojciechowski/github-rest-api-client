package pl.mwojciechowski.github.rest.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A variation of {@link GitHubRepositoryControllerMockE2ETest} that employees the real GitHub servers. A convenient solution
 * for continuous delivery pipeline & smoke tests.
 *
 * @author mwojciechowski
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class GitHubRepositoryControllerRealE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnRepositorySummary() {
        // given
        GitHubRepositorySummary expectedResponse = GitHubRepositorySummary.builder()
                .fullName("spring-cloud/spring-cloud-netflix")
                .description("Integration with Netflix OSS components")
                .cloneUrl("https://github.com/spring-cloud/spring-cloud-netflix.git")
                .build();

        // when
        GitHubRepositorySummary response = restTemplate.getForObject(
                "/repositories/spring-cloud/spring-cloud-netflix", GitHubRepositorySummary.class);

        // then
        assertThat(response.getFullName()).isEqualTo(expectedResponse.getFullName());
        assertThat(response.getDescription()).isEqualTo(expectedResponse.getDescription());
        assertThat(response.getCloneUrl()).isEqualTo(expectedResponse.getCloneUrl());
    }
}
