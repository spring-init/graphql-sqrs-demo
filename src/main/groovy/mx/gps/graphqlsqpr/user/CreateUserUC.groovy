package mx.gps.graphqlsqpr.user

import lombok.RequiredArgsConstructor
import mx.gps.graphqlsqpr.user.DTO.UserDTO
import mx.gps.graphqlsqpr.user.domain.entities.User
import mx.gps.graphqlsqpr.user.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
@RequiredArgsConstructor
class CreateUserUC {

    @Autowired
    private final UserService userService

    @Transactional
    Mono<User> createUser(UserDTO userDTO) {
        userService.createUser(userDTO)
            .flatMap  {user ->
                println 'USER CREATED \n \t\t\t ' + userDTO.username
                //if(userDTO.username.contains('43')) Mono.error( new IllegalArgumentException("Bad username "+ userDTO.username) )
                //else
                Mono.just(user)
//                if(true) Mono.error( new IllegalArgumentException("Bad username "+ userDTO.username) )
            }
    }

}
