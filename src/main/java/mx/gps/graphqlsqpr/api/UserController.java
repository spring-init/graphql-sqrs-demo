package mx.gps.graphqlsqpr.api;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import mx.gps.graphqlsqpr.user.CreateUserUC;
import mx.gps.graphqlsqpr.user.domain.entities.User;
import mx.gps.graphqlsqpr.user.domain.service.UserService;
import mx.gps.graphqlsqpr.user.DTO.UserDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@GraphQLApi
@Component
public class UserController {

    private final UserService userService;
    private final CreateUserUC createUserUC;

    UserController(UserService userService, CreateUserUC createUserUC) {
        this.createUserUC = createUserUC;
        this.userService = userService;
    }

    @GraphQLQuery(name = "sample")
    public Mono<String> getFoods() {
        return Mono.just("foodRepository.findAll()");
    }

    @GraphQLQuery(name = "user")
    public Flux<User> getAllUsers() {
        return userService.getAllUser(PageRequest.of(0,20));
    }


    @GraphQLMutation(name = "createUser")
    public Mono<User> createUser(@GraphQLArgument(name = "user")UserDTO userDTO) {
        return createUserUC.createUser(userDTO);
    }
}
