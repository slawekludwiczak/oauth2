package com.ludigi.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
public class OAuth2ClientController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    private WebClient webClient;

    @GetMapping("/")
    public String index() {
        ClientRegistration springRegistration = this.clientRegistrationRepository.findByRegistrationId("spring");
        return "Hello, client-id is " + springRegistration.getClientId();
    }


    @GetMapping("/token")
    public String token(Authentication authentication) {
        OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientService.loadAuthorizedClient("client1", authentication.getName());

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        return accessToken.getTokenValue();
    }

    @GetMapping("/products")
    public String products() {
        String block = webClient.get()
                .uri("http://localhost:8081/api/products")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(block);
        return "works";
    }
}