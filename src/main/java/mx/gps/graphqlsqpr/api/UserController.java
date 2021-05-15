package mx.gps.graphqlsqpr.api;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import mx.gps.graphqlsqpr.user.CreateUserUC;
import mx.gps.graphqlsqpr.user.DeleteUserUC;
import mx.gps.graphqlsqpr.user.domain.entities.User;
import mx.gps.graphqlsqpr.user.domain.service.UserService;
import mx.gps.graphqlsqpr.user.DTO.UserDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@GraphQLApi
@Component
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CreateUserUC createUserUC;
    private final DeleteUserUC deleteUserUC;

    @GraphQLQuery(name = "sample")
    public Mono<String> getFoods() {
        return Mono.just("foodRepository.findAll()");
    }

    @GraphQLQuery(name = "user")
    public Flux<User> getAllUsers() {
        return userService.getAllUser(PageRequest.of(0,20));
    }


    @GraphQLMutation(name = "createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@GraphQLArgument(name = "user")UserDTO userDTO) {
        return createUserUC.createUser(userDTO)
                .map( user -> {
                    System.out.println(user);
                    return user;
                });
    }

    @GraphQLMutation(name = "deleteUser")
    public Mono<String> deleteUser(@GraphQLArgument(name = "user")UserDTO userDTO) {
        return deleteUserUC.deleteUser(userDTO.getUsername())
                .doOnNext(v ->
                        System.err.println("CONTOLLER DELTE " + v)
            );
    }
}
