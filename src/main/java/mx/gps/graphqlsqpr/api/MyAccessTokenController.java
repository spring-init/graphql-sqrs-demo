package mx.gps.graphqlsqpr.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
class MyAccessTokenController {
    @Autowired
//    @Lazy
//    private OAuth2AuthorizedClientService clientService;
    private ReactiveOAuth2AuthorizedClientService clientService;

//    public Mono<OAuth2AuthorizedClient> extractOAuth2AuthorizedClient(ServerRequest request) {
//        return request.principal()
//                .filter(principal -> principal instanceof OAuth2AuthenticationToken)
//                .cast(OAuth2AuthenticationToken.class)
//                .flatMap(auth -> authorizedClientRepository.loadAuthorizedClient(auth.getAuthorizedClientRegistrationId(), auth, request.exchange()));
//    }

    @RequestMapping("/my-access-token")
    Mono<String> home(Principal user) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) user;
        String authorizedClientRegistrationId = token.getAuthorizedClientRegistrationId();
        String name = user.getName();
//        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(authorizedClientRegistrationId, name);
//        return "token: " + client.getAccessToken().getTokenValue();//
        Mono<OAuth2AuthorizedClient> client = clientService.loadAuthorizedClient(authorizedClientRegistrationId, name);
        return client.map(
                clientP -> "token: " + clientP.getAccessToken().getTokenValue()
        );

//        return extractOAuth2AuthorizedClient(request)
////                .map(client -> "token: " + client.getAccessToken().getTokenValue());
    }
}
