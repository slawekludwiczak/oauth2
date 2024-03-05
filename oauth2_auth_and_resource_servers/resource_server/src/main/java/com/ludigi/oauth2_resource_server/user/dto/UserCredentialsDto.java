package com.ludigi.oauth2_resource_server.user.dto;

import java.util.Set;

public record UserCredentialsDto(String email, String password, Set<String> scopes) {
}
