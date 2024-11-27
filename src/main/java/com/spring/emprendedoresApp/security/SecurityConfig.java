package com.spring.emprendedoresApp.security;

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
        return http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF, ya que no es necesario para una API REST
            .authorizeRequests(authRequest -> authRequest
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Permite acceso sin autenticación a Swagger
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated() // Requiere autenticación para todas las demás rutas
            )
            .build(); // Construye y retorna la configuración de seguridad
    }
}
