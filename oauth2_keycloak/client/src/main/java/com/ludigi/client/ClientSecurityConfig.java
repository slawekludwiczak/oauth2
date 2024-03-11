package com.ludigi.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ClientSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers("/products").authenticated()
                                .requestMatchers("/add-product").authenticated()
                                .requestMatchers("/token").authenticated()
                                .anyRequest().permitAll()
                )
                .oauth2Login(withDefaults());
        return http.build();
    }
}
