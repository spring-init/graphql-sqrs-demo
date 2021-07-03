package mx.gps.graphqlsqpr.config;

import com.nimbusds.oauth2.sdk.TokenIntrospectionResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import graphql.com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
//@Order(1)
public class SecurityConfiguration {

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    // Mimic the default configuration for JWT validation.
//    AuthenticationManager jwt() {
//        // this is the keys endpoint for okta
//        String issuer = oAuth2ClientProperties.getProvider().get("okta").getIssuerUri();
//        String jwkSetUri = issuer + "/v1/keys";
//
//        // This is basically the default jwt logic
//        JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
//        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
//        authenticationProvider.setJwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter());
//        return authenticationProvider::authenticate;
//    }

    // Mimic the default configuration for opaque token validation
    ReactiveAuthenticationManager opaque() {
        String issuer = oAuth2ClientProperties.getProvider().get("okta").getIssuerUri();
        String introspectionUri = issuer + "/v1/introspect";

        // The default opaque token logic
        OAuth2ClientProperties.Registration oktaRegistration = oAuth2ClientProperties.getRegistration().get("okta");
        OpaqueTokenIntrospector introspectionClient = new NimbusOpaqueTokenIntrospector(
                introspectionUri,
                oktaRegistration.getClientId(),
                oktaRegistration.getClientSecret());
        Function<Authentication, Authentication> authenticationFunction = new OpaqueTokenAuthenticationProvider(introspectionClient)::authenticate;
        return authentication -> Mono.just(authenticationFunction.apply(authentication));
    }

    @Bean
    public ReactiveOpaqueTokenIntrospector introspector() {

    String issuer = oAuth2ClientProperties.getProvider().get("okta").getIssuerUri();
    String introspectionUri = issuer + "/v1/introspect";
        OAuth2ClientProperties.Registration oktaRegistration = oAuth2ClientProperties.getRegistration().get("okta");
    return  new NimbusReactiveOpaqueTokenIntrospector(
            introspectionUri,
            oktaRegistration.getClientId(),
            oktaRegistration.getClientSecret()) {
        @Override
        public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
            return super.introspect(token)
                    .map(principal -> {
                        Collection<? extends GrantedAuthority> roles = tokenClaimsToAuthorities(principal.getAttributes(), "roles");

                        ArrayList<GrantedAuthority> authorities = new ArrayList<>(roles);
                        authorities.addAll(principal.getAuthorities());
                        return new OAuth2IntrospectionAuthenticatedPrincipal(principal.getAttributes(),
                                authorities);
                    } );
        }

        Collection<? extends GrantedAuthority> tokenClaimsToAuthorities(Map<String, Object> attributes, String claimKey) {
            if (!CollectionUtils.isEmpty(attributes) && StringUtils.hasText(claimKey)) {
                Object rawRoleClaim = attributes.get(claimKey);
                if (rawRoleClaim instanceof Collection) {
                    return ((Collection<String>) rawRoleClaim).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet());
                } else if (rawRoleClaim != null) { // don't log when null, that is the default condition
                    System.err.printf("Could not extract authorities from claim '%s', value was not a collection", claimKey);
                }
            }
            return Collections.emptySet();
        }
    };

}

    @Bean
        public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
//                .pathMatchers(HttpMethod.POST, "/kayaks/**")
//                .hasAuthority("Admin")
                .anyExchange().authenticated()
                .and().csrf().disable()
                //.oauth2ResourceServer();
        .oauth2ResourceServer().opaqueToken();

      //  http.oauth2ResourceServer().authenticationManagerResolver(exchange -> Mono.just(opaque()) );

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
