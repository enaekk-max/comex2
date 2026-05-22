package com.aduanas.comex.cargams.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // ✅ CORREGIDO: Se quitó el 'http' duplicado que estaba flotando aquí arriba
        http
                // DESACTIVA CSRF
                .csrf(csrf -> csrf.disable())

                // PERMITE TODOS LOS ENDPOINTS (Postman pasará directo)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}