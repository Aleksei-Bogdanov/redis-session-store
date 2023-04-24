package com.company.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) {

        return httpSecurity
                .csrf().disable()
                .httpBasic().and()
                .formLogin().and()
                .authorizeExchange()
                .pathMatchers("/api/persons/registration").permitAll()
                .pathMatchers("/api/persons").hasRole("ADMIN")
                .anyExchange().authenticated()
                .and()
                .build();
    }
}