package pl.mwojciechowski.github.rest.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.io.Resources;
import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

/**
 * A variation of {@link GitHubRepositoryControllerRealE2ETest} that mocks the actual GitHub responses. A convenient solution
 * for continuous integration pipeline.
 * <p/>
 * The following test has one major flaw: dummy GitHub server mocked by WireMock should use a dynamic random port instead
 * of a fixed one. It was done on a purpose for the sake of simplification.
 *
 * @author mwojciechowski
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "github.api.url=http://localhost:9090"
)
public class GitHubRepositoryControllerMockE2ETest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9090);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnRepositorySummary() {
        // given
        stubFor(get(urlEqualTo("/repos/spring-cloud/spring-cloud-netflix"))
                .withHeader("Accept", equalTo("*/*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github-api-example-response.json")));

        GitHubRepositorySummary expectedResponse = GitHubRepositorySummary.builder()
                .fullName("spring-cloud/spring-cloud-netflix")
                .description("Integration with Netflix OSS components")
                .stars(1566)
                .createdAt(LocalDateTime.of(
                        LocalDate.of(2014, 7, 11),
                        LocalTime.of(15, 46, 12)))
                .cloneUrl("https://github.com/spring-cloud/spring-cloud-netflix.git")
                .build();

        // when
        GitHubRepositorySummary response = restTemplate.getForObject(
                "/repositories/spring-cloud/spring-cloud-netflix", GitHubRepositorySummary.class);

        // then
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnRepositorySummaryValidWithContract() throws IOException, JSONException {
        // given
        stubFor(get(urlEqualTo("/repos/spring-cloud/spring-cloud-netflix"))
                .withHeader("Accept", equalTo("*/*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github-api-example-response.json")));

        String expectedResponse = Resources.toString(new ClassPathResource("__files/github-client-example-response.json").getURL(), Charset.forName("UTF-8"));

        // when
        String actualResponse = restTemplate.getForObject(
                "/repositories/spring-cloud/spring-cloud-netflix", String.class);

        // then
        assertEquals(expectedResponse, actualResponse, false);
    }
}
