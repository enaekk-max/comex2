package com.aduanas.com.pagosms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // DESACTIVA CSRF: Vital para que las APIs REST no bloqueen peticiones externas
                .csrf(csrf -> csrf.disable())

                // AUTORIZACIÓN: Permite acceso total para pruebas en Postman y llamadas de pasarelas bancarias
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}