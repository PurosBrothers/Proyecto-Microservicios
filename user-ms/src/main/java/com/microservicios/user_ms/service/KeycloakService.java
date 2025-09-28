package com.microservicios.user_ms.service;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microservicios.user_ms.dto.KeycloakAuthResponse;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KeycloakService {

    @Value("${keycloak.auth-server-url:http://localhost:8081}")
    private String keycloakUrl;

    @Value("${keycloak.realm:proyect-ms-realm}")
    private String realm;

    @Value("${keycloak.admin.username:admin}")
    private String adminUsername;

    @Value("${keycloak.admin.password:admin}")
    private String adminPassword;

    @Value("${keycloak.admin.client-id:admin-cli}")
    private String adminClientId;

    @Value("${keycloak.client.secret:}")
    private String clientSecret;

    private Keycloak keycloak;
    private RealmResource realmResource;

    @PostConstruct
    public void initKeycloak() {
        try {
            this.keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakUrl)
                    .realm("master") // Usar master realm para admin
                    .clientId(adminClientId)
                    .username(adminUsername)
                    .password(adminPassword)
                    .build();

            this.realmResource = keycloak.realm(realm);
            log.info("✅ Conexión con Keycloak establecida correctamente");
        } catch (Exception e) {
            log.error("❌ Error al conectar con Keycloak: {}", e.getMessage());
            // No lanzamos excepción para que la aplicación pueda seguir funcionando
        }
    }

    public String createKeycloakUser(String email, String firstName, String lastName, String password) {
        try {
            if (keycloak == null) {
                log.warn("Keycloak no está disponible, no se puede crear usuario");
                return null;
            }

            UsersResource usersResource = realmResource.users();

            // Crear representación del usuario
            UserRepresentation user = new UserRepresentation();
            user.setUsername(email);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName != null ? lastName : "");
            user.setEnabled(true);
            user.setEmailVerified(true);

            // Crear usuario en Keycloak
            var response = usersResource.create(user);
            
            if (response.getStatus() == 201) {
                // Extraer ID del usuario creado
                String userId = extractUserIdFromLocation(response.getLocation().toString());
                
                // Establecer contraseña
                if (password != null && !password.isEmpty()) {
                    setUserPassword(userId, password);
                }
                
                log.info("✅ Usuario creado en Keycloak con ID: {}", userId);
                return userId;
            } else {
                log.error("❌ Error al crear usuario en Keycloak. Status: {}", response.getStatus());
                return null;
            }
        } catch (Exception e) {
            log.error("❌ Error al crear usuario en Keycloak: {}", e.getMessage());
            return null;
        }
    }

    public boolean updateKeycloakUser(String userId, String email, String firstName, String lastName) {
        try {
            if (keycloak == null) {
                log.warn("Keycloak no está disponible, no se puede actualizar usuario");
                return false;
            }

            UserRepresentation user = realmResource.users().get(userId).toRepresentation();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName != null ? lastName : "");

            realmResource.users().get(userId).update(user);
            log.info("Usuario actualizado en Keycloak: {}", userId);
            return true;
        } catch (Exception e) {
            log.error("Error al actualizar usuario en Keycloak: {}", e.getMessage());
            return false;
        }
    }

    public boolean deleteKeyCloakUser(String userId) {
        try {
            if (keycloak == null) {
                log.warn("Keycloak no está disponible, no se puede eliminar usuario");
                return false;
            }

            realmResource.users().get(userId).remove();
            log.info("Usuario eliminado en Keycloak: {}", userId);
            return true;
        } catch (Exception e) {
            log.error("Error al eliminar usuario en Keycloak: {}", e.getMessage());
            return false;
        }
    }

    private void setUserPassword(String userId, String password) {
        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);

            realmResource.users().get(userId).resetPassword(credential);
            log.info("✅ Contraseña establecida para usuario: {}", userId);
        } catch (Exception e) {
            log.error("❌ Error al establecer contraseña: {}", e.getMessage());
        }
    }

    private String extractUserIdFromLocation(String location) {
        // Location format: http://localhost:8081/admin/realms/mi-realm/users/user-id
        return location.substring(location.lastIndexOf('/') + 1);
    }

    public boolean userExistsInKeycloak(String email) {
        try {
            if (keycloak == null) return false;
            
            List<UserRepresentation> users = realmResource.users().search(email, true);
            return !users.isEmpty();
        } catch (Exception e) {
            log.error("❌ Error al verificar usuario en Keycloak: {}", e.getMessage());
            return false;
        }
    }

    public String getUserKeycloakId(String email) {
        try {
            if (keycloak == null) return null;
            
            List<UserRepresentation> users = realmResource.users().search(email, true);
            if (!users.isEmpty()) {
                return users.get(0).getId();
            }
            return null;
        } catch (Exception e) {
            log.error("❌ Error al obtener ID de usuario en Keycloak: {}", e.getMessage());
            return null;
        }
    }

    public List<UserRepresentation> getAllKeycloakUsers() {
        try {
            if (keycloak == null) return Collections.emptyList();
            
            return realmResource.users().list();
        } catch (Exception e) {
            log.error("❌ Error al obtener usuarios de Keycloak: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean isKeycloakAvailable() {
        return keycloak != null;
    }

    /**
     * Método de debug para verificar configuración de Keycloak
     */
    public String debugKeycloakConfig() {
        StringBuilder debug = new StringBuilder();
        debug.append("🔧 DEBUG KEYCLOAK CONFIG:\n");
        debug.append("keycloakUrl: ").append(keycloakUrl).append("\n");
        debug.append("realm: ").append(realm).append("\n");
        debug.append("adminUsername: ").append(adminUsername).append("\n");
        debug.append("adminClientId: ").append(adminClientId).append("\n");
        debug.append("clientSecret: ").append(clientSecret != null ? "***PRESENTE***" : "NULL").append("\n");
        debug.append("keycloak instance: ").append(keycloak != null ? "CONECTADO" : "NULL").append("\n");
        debug.append("realmResource: ").append(realmResource != null ? "DISPONIBLE" : "NULL").append("\n");
        
        // Probar conexión
        try {
            if (realmResource != null) {
                var users = realmResource.users().list(0, 1);
                debug.append("Test conexión: EXITOSO (").append(users.size()).append(" usuarios encontrados)\n");
            } else {
                debug.append("Test conexión: FALLIDO - realmResource es null\n");
            }
        } catch (Exception e) {
            debug.append("Test conexión: ERROR - ").append(e.getMessage()).append("\n");
        }
        
        return debug.toString();
    }

    /**
     * Autentica usuario con Keycloak y obtiene tokens JWT
     */
    public KeycloakAuthResponse authenticateUser(String email, String password) {
        try {
            if (keycloak == null) {
                log.warn("Keycloak no está disponible para autenticación");
                return null;
            }

            log.info("🔐 Intentando autenticar usuario: {}", email);
            log.info("🔧 Debug - keycloakUrl: {}", keycloakUrl);
            log.info("🔧 Debug - realm: {}", realm);
            log.info("🔧 Debug - clientSecret: {}", clientSecret != null ? "***PRESENTE***" : "NULL");
            
            // Verificar que el usuario existe en Keycloak primero
            boolean userExists = userExistsInKeycloak(email);
            log.info("🔧 Debug - Usuario {} existe en Keycloak: {}", email, userExists);
            if (!userExists) {
                log.warn("❌ Usuario {} no existe en Keycloak", email);
                return null;
            }

            // Usar REST API de Keycloak para autenticación
            String tokenUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";
            
            // Construir el cuerpo de la petición
            String requestBody = "grant_type=password"
                + "&client_id=user-ms-client"
                + "&client_secret=" + clientSecret
                + "&username=" + email
                + "&password=" + password;

            log.info("🌐 Enviando petición de autenticación a: {}", tokenUrl);
            log.info("🔧 Debug - Request body: {}", requestBody.replace(clientSecret, "***SECRET***"));
            
            // Realizar petición HTTP
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            java.net.http.HttpResponse<String> response = client.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                log.info("✅ Autenticación exitosa para usuario: {}", email);
                
                // Parsear respuesta JSON (simple parsing)
                String responseBody = response.body();
                String accessToken = extractJsonValue(responseBody, "access_token");
                String expiresInStr = extractJsonValue(responseBody, "expires_in");
                
                if (accessToken != null && !accessToken.isEmpty()) {
                    long expiresIn = expiresInStr != null ? Long.parseLong(expiresInStr) : 3600;
                    
                    return new KeycloakAuthResponse(
                        accessToken,
                        "refresh_token_placeholder",
                        expiresIn,
                        "Bearer",
                        email
                    );
                }
            } else {
                log.warn("❌ Credenciales inválidas para usuario: {}. Status: {}, Response: {}", 
                    email, response.statusCode(), response.body());
            }

            return null;
        } catch (Exception e) {
            log.error("❌ Error en autenticación para usuario {}: {}", email, e.getMessage());
            return null;
        }
    }

    /**
     * Extrae valor de JSON simple (sin usar librerías externas)
     */
    private String extractJsonValue(String json, String key) {
        try {
            String pattern = "\"" + key + "\":\"";
            int startIndex = json.indexOf(pattern);
            if (startIndex == -1) {
                // Intentar sin comillas (para números)
                pattern = "\"" + key + "\":";
                startIndex = json.indexOf(pattern);
                if (startIndex == -1) return null;
                startIndex += pattern.length();
                int endIndex = json.indexOf(",", startIndex);
                if (endIndex == -1) endIndex = json.indexOf("}", startIndex);
                return json.substring(startIndex, endIndex).trim();
            } else {
                startIndex += pattern.length();
                int endIndex = json.indexOf("\"", startIndex);
                return json.substring(startIndex, endIndex);
            }
        } catch (Exception e) {
            log.error("Error extrayendo valor JSON: {}", e.getMessage());
            return null;
        }
    }


}