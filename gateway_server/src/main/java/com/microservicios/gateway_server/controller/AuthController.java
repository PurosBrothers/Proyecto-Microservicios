package com.microservicios.gateway_server.controller;

import com.microservicios.gateway_server.dto.AuthRequest;
import com.microservicios.gateway_server.dto.KeycloakAuthResponse;
import com.microservicios.gateway_server.dto.RegistroUsuarioDto;
import com.microservicios.gateway_server.service.KeycloakService;
import com.microservicios.gateway_server.service.UserClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final KeycloakService keycloakService;
    private final UserClientService userClientService;

    /**
     * Endpoint para registrar un nuevo usuario
     * El gateway maneja Keycloak y luego crea el usuario en user-ms
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<Map<String, Object>>> registerUser(@Valid @RequestBody RegistroUsuarioDto dto) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Validación adicional de tipo de usuario (obligatorio)
            if (dto.getTipoUsuario() == null) {
                response.put("error", "El tipo de usuario es obligatorio. Debe especificar CLIENTE o PROVEEDOR");
                response.put("field", "tipoUsuario");
                return Mono.just(ResponseEntity.badRequest().body(response));
            }

            log.info("📝 Registrando nuevo usuario: {} como {}", dto.getCorreo(), dto.getTipoUsuario());
            
            // DEBUG: Logging de datos recibidos del frontend
            log.info("🔍 DEBUG - Datos recibidos del frontend:");
            log.info("   - Nombre: '{}'", dto.getNombre());
            log.info("   - Apellido: '{}'", dto.getApellido());
            log.info("   - Correo: '{}'", dto.getCorreo());
            log.info("   - Tipo Usuario: '{}'", dto.getTipoUsuario());
            if ("PROVEEDOR".equals(dto.getTipoUsuario().name())) {
                log.info("   - Página Web: '{}'", dto.getPaginaWeb());
                log.info("   - Redes Sociales: {}", dto.getRedesSociales());
                log.info("   - Redes Sociales (tipo): {}", dto.getRedesSociales() != null ? dto.getRedesSociales().getClass().getSimpleName() : "null");
            }

            // 2. Verificar si el correo ya existe en Keycloak
            if (keycloakService.userExistsInKeycloak(dto.getCorreo())) {
                response.put("error", "El correo ya está registrado");
                response.put("field", "correo");
                return Mono.just(ResponseEntity.badRequest().body(response));
            }

            // 3. Crear usuario en Keycloak con rol específico
            String keycloakUserId = keycloakService.createKeycloakUserWithRole(
                dto.getCorreo(),
                dto.getNombre(),
                dto.getApellido(),
                dto.getPassword(),
                dto.getTipoUsuario()
            );

            if (keycloakUserId == null) {
                response.put("error", "Error al crear usuario en el sistema de autenticación");
                return Mono.just(ResponseEntity.internalServerError().body(response));
            }

            // 4. Crear usuario en el microservicio de usuarios (user-ms)
            return userClientService.createUser(dto, keycloakUserId)
                .then(Mono.fromCallable(() -> {
                    // 5. Preparar respuesta exitosa
                    response.put("message", "Usuario registrado exitosamente");
                    response.put("id", keycloakUserId);
                    response.put("correo", dto.getCorreo());
                    response.put("tipoUsuario", dto.getTipoUsuario());
                    response.put("rolesAsignados", keycloakService.getUserRoles(keycloakUserId));

                    log.info("✅ Usuario {} registrado exitosamente como {}",
                            dto.getCorreo(), dto.getTipoUsuario());

                    return ResponseEntity.ok(response);
                }))
                .onErrorResume(error -> {
                    // Si falla la creación en user-ms, intentar limpiar Keycloak
                    log.error("❌ Error creando usuario en user-ms, limpiando Keycloak: {}", error.getMessage());
                    keycloakService.deleteKeyCloakUser(keycloakUserId);

                    response.put("error", "Error interno del servidor: " + error.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(response));
                });

        } catch (Exception e) {
            log.error("❌ Error registrando usuario {}: {}", dto.getCorreo(), e.getMessage());
            response.put("error", "Error interno del servidor: " + e.getMessage());
            return Mono.just(ResponseEntity.internalServerError().body(response));
        }
    }

    /**
     * Endpoint para autenticar usuario con Keycloak
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {
        try {
            log.info("🔐 Intentando autenticar usuario: {}", authRequest.getEmail());

            KeycloakAuthResponse authResponse = keycloakService.authenticateUser(
                authRequest.getEmail(),
                authRequest.getPassword()
            );

            if (authResponse != null) {
                log.info("✅ Usuario autenticado exitosamente: {}", authRequest.getEmail());
                return ResponseEntity.ok(authResponse);
            } else {
                log.warn("❌ Credenciales inválidas para: {}", authRequest.getEmail());
                return ResponseEntity.badRequest()
                    .body("❌ Credenciales inválidas");
            }

        } catch (Exception e) {
            log.error("❌ Error durante la autenticación: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body("❌ Error durante la autenticación: " + e.getMessage());
        }
    }

    /**
     * Endpoint para verificar tipos de usuario disponibles
     */
    @GetMapping("/tipos-usuario")
    public ResponseEntity<Map<String, Object>> obtenerTiposUsuario() {
        Map<String, Object> response = new HashMap<>();

        Map<String, String> tipos = new HashMap<>();
        for (var tipo : com.microservicios.gateway_server.enums.TipoUsuario.values()) {
            tipos.put(tipo.name(), tipo.getDescripcion());
        }

        response.put("tiposUsuario", tipos);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para verificar si un correo ya está registrado
     */
    @GetMapping("/verificar-correo")
    public ResponseEntity<Map<String, Object>> verificarCorreo(@RequestParam String correo) {
        Map<String, Object> response = new HashMap<>();

        boolean existe = keycloakService.userExistsInKeycloak(correo);
        response.put("existe", existe);
        response.put("correo", correo);

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de debug para verificar configuración de Keycloak
     */
    @GetMapping("/debug-keycloak")
    public ResponseEntity<String> debugKeycloak() {
        try {
            String debugInfo = keycloakService.debugKeycloakConfig();
            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en debug: " + e.getMessage());
        }
    }
}