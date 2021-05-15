package mx.gps.graphqlsqpr.user

import mx.gps.graphqlsqpr.user.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class DeleteUserUC {
    @Autowired
    private UserService userService

    @Transactional
    Mono<String> deleteUser(String username) {
        userService.deleteByUsername(username)
        .flatMap {v ->
            if (v != 1) Mono.error(new IllegalArgumentException("Not delete"))
            else {
                System.err.println("Vale... OK DELETE: " + v)
                Mono.just('OK')
            } }
    }
}
