package com.microservicios.gateway_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuración de seguridad para el Gateway
 * 
 * Esta configuración desactiva la seguridad OAuth2 automática y permite
 * que todas las peticiones pasen libremente a través del gateway.
 * La validación de seguridad se manejará en cada microservicio individual.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            // Desactiva CSRF para APIs REST
            .csrf(csrf -> csrf.disable())
            
            // Desactiva CORS (se maneja en la configuración del gateway)
            .cors(cors -> cors.disable())
            
            // Permite todas las peticiones sin autenticación
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll()
            )
            
            .build();
    }
}