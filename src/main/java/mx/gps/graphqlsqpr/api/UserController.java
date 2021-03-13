package mx.gps.graphqlsqpr.api;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import mx.gps.graphqlsqpr.domain.user.entities.User;
import mx.gps.graphqlsqpr.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@GraphQLApi
@Component
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
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

}
