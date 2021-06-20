package mx.gps.graphqlsqpr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
//                .pathMatchers(HttpMethod.POST, "/kayaks/**")
//                .hasAuthority("Admin")
                .anyExchange().authenticated()
                .and().csrf().disable()
                .oauth2ResourceServer().jwt();
        return http.build();
    }

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        corsConfig.setMaxAge(8000L);
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedHeader("Gabo-Allowed");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

//    @Bean
//    public RouterFunction<ServerResponse> route(MyRestHandler handler){
//        return RouterFunctions
//                .route(
//                        GET("/getToken")
//                                .and(accept(MediaType.ALL)), handler::getToken);
//    }

//    @Component
//    public class MyRestHandler {
//        public Mono<ServerResponse> getToken(ServerRequest serverRequest) {
//            return ServerResponse.ok()
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(getTokenP(serverRequest), String.class);
//        }
//
//        private Mono<String> getTokenP(ServerRequest serverRequest) {
//            return extractOAuth2AuthorizedClient(serverRequest)
//                    .map(client -> "token: " + client.getAccessToken().getTokenValue());
//        }
//    }
//    private ServerOAuth2AuthorizedClientRepository authorizedClientRepository;
//
//    public Mono<OAuth2AuthorizedClient> extractOAuth2AuthorizedClient(ServerRequest request) {
//        return request.principal()
//                .filter(principal -> principal instanceof OAuth2AuthenticationToken)
//                .cast(OAuth2AuthenticationToken.class)
//                .flatMap(auth -> authorizedClientRepository.loadAuthorizedClient(auth.getAuthorizedClientRegistrationId(), auth, request.exchange()));
//    }
}
