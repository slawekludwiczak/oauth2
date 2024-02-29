package com.ludigi.oauth2_resource_server.user;

import com.ludigi.oauth2_resource_server.user.dto.UserCredentialsDto;

import java.util.Set;
import java.util.stream.Collectors;

class UserCredentialsDtoMapper {
    static UserCredentialsDto map(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        Set<String> scopes = user.getScopes()
                .stream()
                .map(UserScope::getName)
                .collect(Collectors.toSet());
        return new UserCredentialsDto(email, password, scopes);
    }
}
