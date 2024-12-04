package com.spring.emprendedoresApp.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.spring.emprendedoresApp.services.IJWTUtilityService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private IJWTUtilityService jwtUtilityService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) // Disables CSRF as it's not needed for a REST API with JWT
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Integrates the CORS configuration
                .authorizeRequests(authRequest -> authRequest.requestMatchers("/auth/**").permitAll() // Allow all for /auth/** routes
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Allow Swagger docs
                        
                        // Routes that require authentication
                        .requestMatchers("/api/users/me").hasAnyRole("ADMIN", "EDITOR")
                        .requestMatchers("/api/users/register/{roleName}").permitAll() // Registration route is publicly accessible
                        .requestMatchers("/api/users").hasRole("ADMIN") // Only ADMIN can get all users
                        .requestMatchers("/api/users/{id}").hasAnyRole("ADMIN", "EDITOR") // Only ADMIN or EDITOR can view user by ID
                        .requestMatchers("/api/users/{id}").hasRole("ADMIN") // Only ADMIN can update user
                        .requestMatchers("/api/users/{id}").hasRole("ADMIN") // Only ADMIN can delete user
                        .requestMatchers("/api/users/page/{page}").permitAll() //hasRole("ADMIN") // Pagination accessible by ADMIN only
                        .requestMatchers("/api/users/{id}").hasAnyRole("ADMIN", "EDITOR") // Partial update accessible by ADMIN or EDITOR
                        .anyRequest().authenticated() // All other routes require authentication
                )
                .sessionManagement(sessionManager -> sessionManager
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
                ) // Stateless sessions, ideal for JWT
                .addFilterBefore(new JWTAuthorizationFilter(jwtUtilityService),
                        UsernamePasswordAuthenticationFilter.class) // Adds the JWT authorization filter before the default authentication filter
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint((request, response, authException) -> 
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                ) // Handles unauthorized errors (401)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Allow requests from localhost:4200
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH")); // Allow these HTTP methods
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Allow these headers
        config.setAllowCredentials(true); // Allow credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply this configuration to all routes

        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(this.corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Set filter to highest precedence to avoid being overridden
        return corsBean;
    }
}
