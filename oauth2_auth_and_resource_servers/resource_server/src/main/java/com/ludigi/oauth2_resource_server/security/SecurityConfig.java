package com.ludigi.oauth2_resource_server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
class SecurityConfig {
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           MvcRequestMatcher.Builder mvc,
                                           AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/products")).access(hasScope("user.write")) //SCOPE_write authority
                .requestMatchers(mvc.pattern("/api/products")).access(hasScope("user.read")) //SCOPE_read authority
                .anyRequest().permitAll()
        );
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrfCustomizer -> csrfCustomizer.disable());
        http.oauth2ResourceServer(config -> config.jwt(Customizer.withDefaults()));
        return http.build();
    }
}