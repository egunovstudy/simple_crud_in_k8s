package com.gegunov.openapi.api;

import com.gegunov.config.KeycloakUser;
import com.gegunov.mapper.UserMapper;
import com.gegunov.openapi.model.User;
import jakarta.annotation.PostConstruct;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;

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
    public User getUserInfo(Authentication authentication) {
        KeycloakUser keycloakUser = restClient.get()
                .uri(accountUrl)
                .header("Authorization", "Bearer " + ((Jwt) authentication.getPrincipal()).getTokenValue())
                .header("Content-Type", "application/json")
                .retrieve()
                .toEntity(KeycloakUser.class)
                .getBody();
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        User user = mapper.keyCloackUserToUserDto(keycloakUser);
        user.setPhone(((ArrayList<?>) keycloakUser.getAttributes().get("phone")).get(0).toString());
        return user;
    }

    @PutMapping(path = "/users/info")
    public void updateUserDetails(@RequestBody User user, Authentication authentication) {
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        KeycloakUser keycloakUser = mapper.userDtoToKeyCloackUser(user);
        keycloakUser.getAttributes().put("phone", user.getPhone());
        restClient.post()
                .uri(accountUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(keycloakUser)
                .header("Authorization", "Bearer " + ((Jwt) authentication.getPrincipal()).getTokenValue())
                .retrieve()
                .toBodilessEntity();
    }

}
