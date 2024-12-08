package com.gegunov.billing.web;

import com.gegunov.billing.web.model.KeycloakUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class AuthInfoController {

    @Value("${user.account.url}")
    private String accountUrl;

    private RestClient restClient;

    @PostConstruct
    public void setUp() {
        restClient = RestClient.create();
    }

    @GetMapping(path = "/users/info")
    public KeycloakUser getUserInfo(Authentication authentication) {
        return restClient.get()
                .uri(accountUrl)
                .header("Authorization", "Bearer " + ((Jwt) authentication.getPrincipal()).getTokenValue())
                .header("Content-Type", "application/json")
                .retrieve()
                .toEntity(KeycloakUser.class)
                .getBody();
    }

}
