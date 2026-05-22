package com.aduanas.com.documentosms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // DESACTIVA CSRF (Esencial para APIs REST sin estado)
                .csrf(csrf -> csrf.disable())

                // PERMITE TODOS LOS ENDPOINTS (Acceso total para Postman y comunicación Inter-MS)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}