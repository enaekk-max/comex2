package com.aduanas.comex.notificacion_ms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // ✅ CORREGIDO: Formateado limpio en cascada para mejor legibilidad
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF porque no usamos sesiones de navegador (cookies)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permitimos libre tránsito entre microservicios
                );

        // ======================================================
        // CONSTRUIR CONFIG
        // ======================================================
        return http.build();
    }
}