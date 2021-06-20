package mx.gps.graphqlsqpr.api

import mx.gps.graphqlsqpr.UserRepository
import mx.gps.graphqlsqpr.user.DTO.UserDTO
import mx.gps.graphqlsqpr.user.domain.entities.User
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("module-test")
@Stepwise
@WithMockUser(authorities = ['MASTER'])
class UserControllerCmpTest extends Specification {
    @Autowired
    private WebTestClient webClient
    @Autowired
    private UserRepository userRepository
    @Shared
    private String username

    def "get All users"() {
        def user1 = new User(name: "Juan Penas")
        given:
        //userRepository.save(user1).block(Duration.of(1, ChronoUnit.SECONDS));
        when: 'get User info'
        def exchange = webClient.post().uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("""{\"query\":\"{user {id, name}}\"}"""))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
        then:
        exchange.expectStatus().isOk()
                .expectBody(Map)
                .consumeWith {
                    List users = it.getResponseBody().data.user
                    Assertions.assertThat(users).isNotEmpty()
                    users.forEach { Assertions.assertThat(it.id).isGreaterThan(0) }
                }
    }

    def "create New User"() {
        given: 'new User'
        def user = new UserDTO(username: "User_" + System.currentTimeMillis() + "@mail.com")
        username = user.username
        when: "call create new User"
        String request = """
            {\"query\": 
                \"mutation {createUser (user: {username: \\"${username}\\"}) {id name}}\"
            }
        """
        println request
        def exchange = webClient.post().uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
        then: 'new User is created'
        //User userResp =
        def exchangebody = exchange.expectStatus().isOk()
                .expectBody()
        exchangebody.jsonPath('$.data.createUser.id').isNumber()
        exchangebody.jsonPath('$.errors').doesNotExist()
    }

    @WithMockUser(authorities = ['ROOT'])
    def "delete User"() {
        given: 'username'

        when: "call delete User"
        String request = """
            {\"query\": 
                \"mutation {deleteUser (user: {username: \\"${username}\\"})}\"
            }
        """
        println request
        def exchange = webClient.post().uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()

        then: 'User is delete'
        def body = exchange.expectStatus().isOk()
                .expectBody()
        body
                .jsonPath('$.data.deleteUser').isEqualTo("OK")
            body    .jsonPath('$.errors').doesNotExist()
    }
}
