package com.aduana.bancoms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para poder hacer POST/PUT sin problemas
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // DEJA PASAR TODO SIN CONTRASEÑA
                );
        return http.build();
    }
}