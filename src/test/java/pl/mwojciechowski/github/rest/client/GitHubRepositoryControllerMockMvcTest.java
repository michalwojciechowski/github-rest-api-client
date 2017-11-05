package pl.mwojciechowski.github.rest.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mwojciechowski
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class GitHubRepositoryControllerMockMvcTest {

    @MockBean
    private GitHubClient gitHubClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldPreserveResponseContract() throws Exception {
        // given
        GitHubRepositorySummary summary = GitHubRepositorySummary.builder().
                stars(99)
                .createdAt(LocalDateTime.now())
                .cloneUrl("dummy-invalid-url")
                .fullName("dummy full name")
                .description("dummy description").build();

        String owner = "dummy-user";
        String repository = "dummy-repository";

        given(gitHubClient.findRepositoryBy(owner, repository)).willReturn(summary);

        // when
        ResultActions result = mockMvc.perform(get("/repositories/{owner}/{repositoryName}", owner, repository));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(summary.getFullName())))
                .andExpect(jsonPath("$.description", is(summary.getDescription())))
                .andExpect(jsonPath("$.stars", is(summary.getStars())))
                .andExpect(jsonPath("$.createdAt", is(DateTimeFormatter.ISO_DATE_TIME.format(summary.getCreatedAt()))));
    }

    @Test
    public void shouldReturnStatusCode500IfExceptionOccurs() throws Exception {
        // given
        String owner = "dummy-user";
        String repository = "dummy-repository";

        given(gitHubClient.findRepositoryBy(owner, repository)).willThrow(new RuntimeException("Something bad happened!"));

        // when
        ResultActions result = mockMvc.perform(get("/repositories/{owner}/{repositoryName}", owner, repository));

        // then
        result.andDo(print()).andExpect(status().is5xxServerError());
    }
}
