package com.ludigi.auth_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

@Configuration
public class AuthServerSecurityConfiguration {
    @Autowired
    private RegisteredClientRepository registeredClientRepository;

}
