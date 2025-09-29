package com.microservicios.gateway_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configuración de seguridad para el Gateway
 *
 * Configura validación JWT para endpoints protegidos y permite acceso público
 * a endpoints de autenticación.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${keycloak.auth-server-url:http://localhost:8081}")
    private String keycloakUrl;

    @Value("${keycloak.realm:proyect-ms-realm}")
    private String realm;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            // Desactiva CSRF para APIs REST
            .csrf(csrf -> csrf.disable())

            // Desactiva CORS (se maneja en la configuración del gateway)
            .cors(cors -> cors.disable())

            // Configura autorización
            .authorizeExchange(exchanges -> exchanges
                // Endpoints públicos de autenticación
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers("/actuator/**").permitAll()

                // Todas las demás rutas requieren autenticación JWT
                .anyExchange().authenticated()
            )

            // Configura OAuth2 Resource Server con JWT
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
            )

            .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        String jwkSetUri = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/certs";
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}