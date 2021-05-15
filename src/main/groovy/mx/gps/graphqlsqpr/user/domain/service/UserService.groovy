package mx.gps.graphqlsqpr.user.domain.service

import mx.gps.graphqlsqpr.UserRepository
import mx.gps.graphqlsqpr.user.DTO.UserDTO
import mx.gps.graphqlsqpr.user.domain.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService {

    @Autowired
    private final UserRepository userRepository

    Flux<User> getAllUser(final Pageable pageable) {
        userRepository.findAll()
    }

  //  @Transactional(propagation = Propagation.MANDATORY)
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    Mono<User> createUser(UserDTO userDTO) {
        userRepository.save(new User(name: userDTO.username))
    }

    Mono<Integer> deleteByUsername(String username) {
        userRepository.deleteByName(username)
    }
}
