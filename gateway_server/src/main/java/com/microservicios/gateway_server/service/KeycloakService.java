package com.microservicios.gateway_server.service;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microservicios.gateway_server.dto.KeycloakAuthResponse;
import com.microservicios.gateway_server.enums.TipoUsuario;
import org.keycloak.representations.idm.RoleRepresentation;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

@Service
@Slf4j
public class KeycloakService {

    @Value("${keycloak.auth-server-url:http://keycloak:8080}")
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

    /**
     * Crea un usuario en Keycloak con rol específico
     */
    public String createKeycloakUserWithRole(String email, String firstName, String lastName, String password, TipoUsuario tipoUsuario) {
        try {
            // 1. Crear usuario normal
            String userId = createKeycloakUser(email, firstName, lastName, password);
            if (userId == null) {
                log.error("No se pudo crear usuario en Keycloak");
                return null;
            }

            // 2. Crear roles si no existen
            initializeRoles();

            // 3. Asignar rol según tipo de usuario
            boolean roleAssigned = assignRoleToUser(userId, tipoUsuario);
            if (!roleAssigned) {
                log.warn("Usuario creado pero no se pudo asignar el rol {}", tipoUsuario);
            }

            log.info("✅ Usuario {} creado en Keycloak con rol {}", email, tipoUsuario);
            return userId;

        } catch (Exception e) {
            log.error("❌ Error creando usuario con rol en Keycloak: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Inicializa los roles CLIENTE y PROVEEDOR en Keycloak si no existen
     */
    private void initializeRoles() {
        try {
            if (keycloak == null || realmResource == null) {
                log.warn("Keycloak no disponible para inicializar roles");
                return;
            }

            // Crear rol CLIENTE
            createRoleIfNotExists("CLIENTE", "Usuario que consume servicios");

            // Crear rol PROVEEDOR
            createRoleIfNotExists("PROVEEDOR", "Usuario que ofrece servicios");

        } catch (Exception e) {
            log.error("❌ Error inicializando roles: {}", e.getMessage());
        }
    }

    /**
     * Crea un rol en Keycloak si no existe
     */
    private void createRoleIfNotExists(String roleName, String description) {
        try {
            var rolesResource = realmResource.roles();

            // Verificar si el rol ya existe
            try {
                rolesResource.get(roleName).toRepresentation();
                log.debug("Rol {} ya existe en Keycloak", roleName);
                return;
            } catch (Exception e) {
                // El rol no existe, lo creamos
            }

            // Crear el rol
            RoleRepresentation role = new RoleRepresentation();
            role.setName(roleName);
            role.setDescription(description);

            rolesResource.create(role);
            log.info("✅ Rol {} creado en Keycloak", roleName);

        } catch (Exception e) {
            log.error("❌ Error creando rol {}: {}", roleName, e.getMessage());
        }
    }

    /**
     * Asigna un rol a un usuario
     */
    private boolean assignRoleToUser(String userId, TipoUsuario tipoUsuario) {
        try {
            if (keycloak == null || realmResource == null) {
                log.warn("Keycloak no disponible para asignar roles");
                return false;
            }

            String roleName = tipoUsuario.getNombre();

            // Obtener representación del rol
            RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();

            // Asignar rol al usuario
            realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(role));

            log.info("✅ Rol {} asignado al usuario {}", roleName, userId);
            return true;

        } catch (Exception e) {
            log.error("❌ Error asignando rol {} al usuario {}: {}",
                      tipoUsuario.getNombre(), userId, e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene los roles de un usuario
     */
    public List<String> getUserRoles(String userId) {
        try {
            if (keycloak == null || realmResource == null) {
                log.warn("Keycloak no disponible para obtener roles");
                return Collections.emptyList();
            }

            var roles = realmResource.users().get(userId).roles().realmLevel().listAll();
            return roles.stream()
                        .map(RoleRepresentation::getName)
                        .filter(name -> name.equals("CLIENTE") || name.equals("PROVEEDOR"))
                        .toList();

        } catch (Exception e) {
            log.error("❌ Error obteniendo roles del usuario {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Verifica si un usuario tiene un rol específico
     */
    public boolean userHasRole(String userId, TipoUsuario tipoUsuario) {
        try {
            List<String> userRoles = getUserRoles(userId);
            return userRoles.contains(tipoUsuario.getNombre());
        } catch (Exception e) {
            log.error("❌ Error verificando rol {} para usuario {}: {}",
                      tipoUsuario.getNombre(), userId, e.getMessage());
            return false;
        }
    }

    /**
     * Asigna un rol a un usuario existente en Keycloak
     * Método público para uso desde DbInitializer
     */
    public boolean assignRoleToExistingUser(String userId, TipoUsuario tipoUsuario) {
        try {
            // Verificar si el usuario ya tiene el rol
            if (userHasRole(userId, tipoUsuario)) {
                log.info("Usuario {} ya tiene el rol {}", userId, tipoUsuario.getNombre());
                return true;
            }

            // Crear roles si no existen
            initializeRoles();

            // Asignar el rol
            boolean result = assignRoleToUser(userId, tipoUsuario);
            if (result) {
                log.info("✅ Rol {} asignado correctamente a usuario existente {}",
                        tipoUsuario.getNombre(), userId);
            }
            return result;

        } catch (Exception e) {
            log.error("❌ Error asignando rol {} a usuario existente {}: {}",
                      tipoUsuario.getNombre(), userId, e.getMessage());
            return false;
        }
    }

}