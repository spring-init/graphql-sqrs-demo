package mx.gps.graphqlsqpr.user.domain.service

import mx.gps.graphqlsqpr.UserRepository
import mx.gps.graphqlsqpr.user.DTO.UserDTO
import mx.gps.graphqlsqpr.user.domain.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService {

    @Autowired
    private  UserRepository userRepository

    Flux<User> getAllUser(final Pageable pageable) {
        userRepository.findAll()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Mono<User> createUser(UserDTO userDTO) {
        userRepository.save(new User(name: userDTO.username))
    }
}
