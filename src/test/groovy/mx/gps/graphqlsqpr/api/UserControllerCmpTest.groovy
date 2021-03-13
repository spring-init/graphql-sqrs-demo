package mx.gps.graphqlsqpr.api

import mx.gps.graphqlsqpr.UserRepository
import mx.gps.graphqlsqpr.domain.user.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import spock.lang.Specification

@SpringBootTest
@AutoConfigureWebTestClient
//@AutoConfigureDataR2dbc
class UserControllerCmpTest extends Specification {
    @Autowired
    private WebTestClient webClient
    @Autowired
    private UserRepository userRepository

    def "get All users"() {
        System.err.println("ASDFASDF aSDFAS" + webClient)
        def user1 = new User(name: "Juan Penas")
        given:
        //userRepository.save(user1).block(Duration.of(1, ChronoUnit.SECONDS));
        String username = "User1"

        when: 'get User info'
        def exchange = webClient.post().uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("""{\"query\":\"{user {id, name}}\"}"""))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
        then:
                exchange.expectStatus().isOk()
                .expectBody()
                .jsonPath('$.data').exists()
               /** .findAll().forEach { user ->
                    Assertions.assertThat user isNot null
                };*/
    }
}
