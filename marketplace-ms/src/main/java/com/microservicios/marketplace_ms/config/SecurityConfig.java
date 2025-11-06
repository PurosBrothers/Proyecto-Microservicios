package com.microservicios.marketplace_ms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para el microservicio marketplace
 * Integra con Keycloak para autenticación y autorización JWT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                // Endpoints públicos (solo lectura)
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()  // Solo para desarrollo
                .requestMatchers("/api/debug/**").permitAll()  // Debug endpoints - solo desarrollo
                
                // Endpoints de lectura - acceso público o autenticado
                .requestMatchers("GET", "/alojamiento").permitAll()
                .requestMatchers("GET", "/alojamiento/{id}").permitAll()
                .requestMatchers("GET", "/alimentacion").permitAll()
                .requestMatchers("GET", "/alimentacion/{id}").permitAll()
                .requestMatchers("GET", "/transporte").permitAll()
                .requestMatchers("GET", "/transporte/{id}").permitAll()
                .requestMatchers("GET", "/paseos-ecologicos").permitAll()
                .requestMatchers("GET", "/paseos-ecologicos/{id}").permitAll()
                
                // Endpoints de escritura - solo proveedores autenticados
                .requestMatchers("POST", "/alojamiento").hasRole("PROVEEDOR")
                .requestMatchers("PUT", "/alojamiento/**").hasRole("PROVEEDOR")
                .requestMatchers("DELETE", "/alojamiento/**").hasRole("PROVEEDOR")
                
                .requestMatchers("POST", "/alimentacion").hasRole("PROVEEDOR")
                .requestMatchers("PUT", "/alimentacion/**").hasRole("PROVEEDOR")
                .requestMatchers("DELETE", "/alimentacion/**").hasRole("PROVEEDOR")
                
                .requestMatchers("POST", "/transporte").hasRole("PROVEEDOR")
                .requestMatchers("PUT", "/transporte/**").hasRole("PROVEEDOR")
                .requestMatchers("DELETE", "/transporte/**").hasRole("PROVEEDOR")
                
                .requestMatchers("POST", "/paseos-ecologicos").hasRole("PROVEEDOR")
                .requestMatchers("PUT", "/paseos-ecologicos/**").hasRole("PROVEEDOR")
                .requestMatchers("DELETE", "/paseos-ecologicos/**").hasRole("PROVEEDOR")
                
                // Endpoints por usuario - requiere autenticación
                .requestMatchers("/*/usuario/**").authenticated()
                
                // Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder())
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // Para H2 console

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
        return authenticationConverter;
    }
}