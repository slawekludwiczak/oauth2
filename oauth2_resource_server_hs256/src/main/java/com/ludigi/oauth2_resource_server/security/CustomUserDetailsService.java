package com.ludigi.oauth2_resource_server.security;

import com.ludigi.oauth2_resource_server.user.UserCredentialsService;
import com.ludigi.oauth2_resource_server.user.dto.UserCredentialsDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class CustomUserDetailsService implements UserDetailsService {
    private final UserCredentialsService userService;

    public CustomUserDetailsService(UserCredentialsService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findCredentialsByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
    }

    private UserDetails createUserDetails(UserCredentialsDto credentials) {
        return User.builder()
                .username(credentials.email())
                .password(credentials.password())
                .authorities(credentials.scopes().toArray(String[]::new))
                .build();
    }
}