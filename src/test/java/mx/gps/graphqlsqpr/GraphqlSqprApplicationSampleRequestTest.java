package mx.gps.graphqlsqpr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest
@AutoConfigureWebTestClient
class GraphqlSqprApplicationSampleRequestTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void sample() {
        String expectedResponse = "{\"data\":{\"sample\":\"foodRepository.findAll()\"}}";
        webClient.post().uri("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue("{\"query\":\"{sample}\"}"))
                        .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(expectedResponse);
    }

}
