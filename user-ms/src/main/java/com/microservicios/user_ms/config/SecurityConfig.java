package com.microservicios.user_ms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Permitir OPTIONS para CORS preflight
                .requestMatchers("OPTIONS", "/**").permitAll()
                // Endpoints públicos (no requieren autenticación)
                .requestMatchers("/users/authenticate", "/users/info", "/users", "/h2-console/**").permitAll()
                .requestMatchers("/users/internal/**").permitAll() // Endpoints internos - sin autenticación
                .requestMatchers("/users/{id}").permitAll() // GET usuario por ID - público para facilitar pruebas
                .requestMatchers("POST", "/users").permitAll() // Crear usuario - público
                // Endpoints protegidos (requieren JWT)
                .requestMatchers("PUT", "/users/**").authenticated()
                .requestMatchers("DELETE", "/users/**").authenticated()
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwkSetUri("http://localhost:8081/realms/proyect-ms-realm/protocol/openid-connect/certs"))
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/users/**") // Deshabilitar CSRF para H2 y users
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Permitir frames para H2 console
            );
        return http.build();
    }


}