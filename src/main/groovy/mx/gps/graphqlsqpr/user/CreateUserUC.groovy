package mx.gps.graphqlsqpr.user

import mx.gps.graphqlsqpr.user.domain.entities.User
import mx.gps.graphqlsqpr.user.domain.service.UserService
import mx.gps.graphqlsqpr.user.DTO.UserDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreateUserUC {

    private final UserService userService;

    Mono<User> createUser(UserDTO userDTO) {
        def user = userService.createUser(userDTO)
        if(userDTO.username.contains('4')) throw new IllegalArgumentException("Bad username "+ userDTO.username)
        user
    }

}
