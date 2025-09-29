package com.microservicios.gateway_server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para respuesta de autenticación de Keycloak
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakAuthResponse {

    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private String tokenType;
    private String email;
}