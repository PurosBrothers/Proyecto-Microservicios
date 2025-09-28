package com.microservicios.user_ms.controller;


import com.microservicios.user_ms.dto.RegistroUsuarioDto;
import com.microservicios.user_ms.dto.UsuarioDTO;
import com.microservicios.user_ms.dto.KeycloakAuthResponse;
import com.microservicios.user_ms.dto.AuthRequest;
import com.microservicios.user_ms.enums.TipoUsuario;
import com.microservicios.user_ms.service.UsuarioService;
import com.microservicios.user_ms.service.KeycloakService;
import com.microservicios.user_ms.entity.Cliente;
import com.microservicios.user_ms.entity.Proveedor;
import com.microservicios.user_ms.repository.UsuarioRepository;
import com.microservicios.user_ms.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UsuarioService usuarioService;
    private final KeycloakService keycloakService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    /**
     * Crear nuevo usuario con rol obligatorio - Se crea automáticamente en Keycloak y en la base local
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody RegistroUsuarioDto dto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 1. Validación adicional de tipo de usuario (obligatorio)
            if (dto.getTipoUsuario() == null) {
                response.put("error", "El tipo de usuario es obligatorio. Debe especificar CLIENTE o PROVEEDOR");
                response.put("field", "tipoUsuario");
                return ResponseEntity.badRequest().body(response);
            }

            log.info("📝 Registrando nuevo usuario: {} como {}", dto.getCorreo(), dto.getTipoUsuario());

            // 2. Verificar si el correo ya existe
            if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
                response.put("error", "El correo ya está registrado");
                response.put("field", "correo");
                return ResponseEntity.badRequest().body(response);
            }

            // 3. Crear usuario en Keycloak con rol específico
            String keycloakUserId = null;
            if (keycloakService.isKeycloakAvailable()) {
                String[] nombres = dto.getNombre().split(" ", 2);
                String firstName = nombres[0];
                String lastName = nombres.length > 1 ? nombres[1] : "";
                
                keycloakUserId = keycloakService.createKeycloakUserWithRole(
                    dto.getCorreo(), 
                    firstName, 
                    lastName, 
                    dto.getPassword(),
                    dto.getTipoUsuario()
                );
                
                if (keycloakUserId == null) {
                    response.put("error", "Error al crear usuario en el sistema de autenticación");
                    return ResponseEntity.internalServerError().body(response);
                }
            } else {
                log.warn("Keycloak no disponible, usuario se creará solo en base de datos local");
            }

            // 4. Crear usuario en base de datos local
            var usuario = crearUsuarioSegunTipo(dto, keycloakUserId);
            usuarioRepository.save(usuario);

            // 5. Preparar respuesta exitosa
            response.put("message", "Usuario registrado exitosamente");
            response.put("id", usuario.getId());
            response.put("correo", usuario.getCorreo());
            response.put("tipoUsuario", usuario.getTipoUsuario());
            response.put("keycloakId", keycloakUserId);
            
            if (keycloakService.isKeycloakAvailable()) {
                response.put("rolesAsignados", keycloakService.getUserRoles(keycloakUserId));
            }

            log.info("✅ Usuario {} registrado exitosamente como {}", 
                    dto.getCorreo(), dto.getTipoUsuario());
                    
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Error registrando usuario {}: {}", dto.getCorreo(), e.getMessage());
            response.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Crea la instancia de usuario según el tipo especificado
     */
    private com.microservicios.user_ms.entity.Usuario crearUsuarioSegunTipo(RegistroUsuarioDto dto, String keycloakUserId) {
        // Validación adicional de seguridad
        if (dto.getTipoUsuario() == null) {
            throw new IllegalArgumentException("El tipo de usuario es obligatorio y no puede ser nulo");
        }
        
        com.microservicios.user_ms.entity.Usuario usuario;
        
        switch (dto.getTipoUsuario()) {
            case CLIENTE:
                Cliente cliente = new Cliente();
                usuario = cliente;
                break;
                
            case PROVEEDOR:
                Proveedor proveedor = new Proveedor();
                usuario = proveedor;
                break;
                
            default:
                throw new IllegalArgumentException("Tipo de usuario no válido: " + dto.getTipoUsuario());
        }

        // Configurar campos comunes
        usuario.setId(keycloakUserId != null ? keycloakUserId : UUID.randomUUID().toString());
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setEdad(dto.getEdad());
        usuario.setDescripcion(dto.getDescripcion());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTipoUsuario(dto.getTipoUsuario());

        return usuario;
    }

    /**
     * Endpoint para verificar tipos de usuario disponibles
     */
    @GetMapping("/tipos-usuario")
    public ResponseEntity<Map<String, Object>> obtenerTiposUsuario() {
        Map<String, Object> response = new HashMap<>();
        
        Map<String, String> tipos = new HashMap<>();
        for (TipoUsuario tipo : TipoUsuario.values()) {
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
        
        boolean existe = usuarioRepository.findByCorreo(correo).isPresent();
        response.put("existe", existe);
        response.put("correo", correo);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener usuario por ID (no requiere autenticación para facilitar pruebas)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            UsuarioDTO usuario = usuarioService.getUsuario(id);
            if (usuario != null) {
                log.info("✅ Usuario encontrado: {}", usuario.getCorreo());
                return ResponseEntity.ok(usuario);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("❌ Error al obtener usuario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body("❌ Error al obtener usuario: " + e.getMessage());
        }
    }

    /**
     * Obtener todos los usuarios (para facilitar pruebas)
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsers() {
        try {
            List<UsuarioDTO> usuarios = usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
            
            log.info("✅ Obtenidos {} usuarios", usuarios.size());
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            log.error("❌ Error al obtener usuarios: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualizar usuario (requiere autenticación JWT)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_profile') or hasAuthority('SCOPE_email')")
    public ResponseEntity<?> updateUser(@PathVariable String id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {

            String[] nombreParts = usuarioDTO.getNombre().split(" ", 2);
            String firstName = nombreParts[0];
            String lastName = nombreParts.length > 1 ? nombreParts[1] : "";
            
            boolean keycloakUpdated = keycloakService.updateKeycloakUser(
                id, usuarioDTO.getCorreo(), firstName, lastName
            );
            
            if (!keycloakUpdated) {
                return ResponseEntity.badRequest()
                    .body("No se pudo actualizar el usuario en Keycloak, operación abortada");
            }

            UsuarioDTO updated = usuarioService.updateUsuario(id, usuarioDTO);
            if (updated != null) {
                log.info("✅ Usuario actualizado: {}", updated.getCorreo());
                return ResponseEntity.ok(updated);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("❌ Error al actualizar usuario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body("❌ Error al actualizar usuario: " + e.getMessage());
        }
    }

    /**
     * Eliminar usuario (requiere autenticación JWT)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_profile') or hasAuthority('SCOPE_email')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            boolean keycloakDeleted = keycloakService.deleteKeyCloakUser(id);
            if (!keycloakDeleted) {
                return ResponseEntity.badRequest()
                    .body("No se pudo eliminar el usuario en Keycloak, operación abortada");
            }
            UsuarioDTO deleted = usuarioService.deleteUsuario(id);
            if (deleted != null) {
                log.info("✅ Usuario eliminado: {}", deleted.getCorreo());
                return ResponseEntity.ok(deleted);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("❌ Error al eliminar usuario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body("❌ Error al eliminar usuario: " + e.getMessage());
        }
    }

    /**
     * Endpoint de información del sistema (para debug)
     */
    @GetMapping("/info")
    public ResponseEntity<?> getSystemInfo() {
        return ResponseEntity.ok(new SystemInfo(
            usuarioRepository.count(),
            keycloakService.isKeycloakAvailable(),
            "Keycloak integrado - UUIDs generados automáticamente"
        ));
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

    /**
     * Endpoint para autenticar usuario con Keycloak
     * Este endpoint facilita la autenticación desde el frontend sin necesidad de comunicación directa con Keycloak
     */
    @PostMapping("/authenticate")
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

    // Clase interna para información del sistema
    public static class SystemInfo {
        public final long totalUsers;
        public final boolean keycloakAvailable;
        public final String message;

        public SystemInfo(long totalUsers, boolean keycloakAvailable, String message) {
            this.totalUsers = totalUsers;
            this.keycloakAvailable = keycloakAvailable;
            this.message = message;
        }
    }
}