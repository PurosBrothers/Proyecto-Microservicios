package com.microservicios.marketplace_ms.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.beans.factory.annotation.Value;

/**
 * Servicio para validar usuarios con el microservicio user-ms
 */
@Service
public class UserValidationService {

    @Value("${user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public UserValidationService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Valida que el usuario existe y es un proveedor
     * @param usuarioId ID del usuario a validar
     * @return true si es un proveedor válido, false en caso contrario
     */
    public boolean isValidProveedor(String usuarioId) {
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            return false;
        }

        try {
            // Llamada al user-ms para obtener información del usuario
            String url = userServiceUrl + "/usuarios/" + usuarioId;
            
            // Intentar obtener el usuario
            UserResponse userResponse = restTemplate.getForObject(url, UserResponse.class);
            
            if (userResponse == null) {
                return false;
            }

            // Verificar que el tipo de usuario sea PROVEEDOR
            return "PROVEEDOR".equalsIgnoreCase(userResponse.getTipoUsuario());
            
        } catch (RestClientException e) {
            // Log del error si es necesario
            System.err.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida que el usuario existe
     * @param usuarioId ID del usuario a validar
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean userExists(String usuarioId) {
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            return false;
        }

        try {
            String url = userServiceUrl + "/usuarios/" + usuarioId;
            UserResponse userResponse = restTemplate.getForObject(url, UserResponse.class);
            return userResponse != null;
            
        } catch (RestClientException e) {
            System.err.println("Error al verificar existencia del usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clase interna para mapear la respuesta del user-ms
     */
    public static class UserResponse {
        private String id;
        private String nombre;
        private String tipoUsuario;

        public UserResponse() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getTipoUsuario() {
            return tipoUsuario;
        }

        public void setTipoUsuario(String tipoUsuario) {
            this.tipoUsuario = tipoUsuario;
        }
    }
}