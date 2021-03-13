package mx.gps.graphqlsqpr.domain.user.service

import mx.gps.graphqlsqpr.domain.user.entities.User
import mx.gps.graphqlsqpr.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class UserService {

    @Autowired
    private  UserRepository userRepository

    Flux<User> getAllUser(final Pageable pageable) {
        userRepository.findAll()
    }
}
