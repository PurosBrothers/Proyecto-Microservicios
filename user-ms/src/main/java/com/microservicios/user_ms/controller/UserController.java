package com.microservicios.user_ms.controller;

import com.microservicios.user_ms.dto.CreateUserRequestDTO;
import com.microservicios.user_ms.dto.UsuarioDTO;
import com.microservicios.user_ms.dto.KeycloakAuthResponse;
import com.microservicios.user_ms.dto.AuthRequest;
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

import java.util.List;
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
     * Crear nuevo usuario - Se crea automáticamente en Keycloak y en la base local
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        try {
            log.info("📝 Creando nuevo usuario: {}", request.getCorreo());
            
            // Verificar si el usuario ya existe en Keycloak
            if (keycloakService.userExistsInKeycloak(request.getCorreo())) {
                return ResponseEntity.badRequest()
                    .body("❌ El usuario ya existe en Keycloak con email: " + request.getCorreo());
            }

            // Crear usuario en Keycloak primero
            String keycloakId = keycloakService.createKeycloakUser(
                request.getCorreo(),
                request.getNombre().split(" ")[0], // Primer nombre
                request.getNombre().contains(" ") ? request.getNombre().substring(request.getNombre().indexOf(" ") + 1) : "", // Apellidos
                request.getPassword()
            );

            if (keycloakId == null) {
                return ResponseEntity.badRequest()
                    .body("❌ Error al crear usuario en Keycloak");
            }

            // Crear usuario en base de datos local
            if ("CLIENTE".equalsIgnoreCase(request.getTipoUsuario())) {
                Cliente cliente = new Cliente();
                cliente.setId(keycloakId);
                cliente.setNombre(request.getNombre());
                cliente.setEdad(request.getEdad());
                cliente.setFotoUrl(request.getFotoUrl());
                cliente.setDescripcion(request.getDescripcion());
                cliente.setCorreo(request.getCorreo());
                cliente.setDireccion(request.getDireccion());
                cliente.setTelefono(request.getTelefono());
                
                Cliente savedCliente = usuarioRepository.save(cliente);
                UsuarioDTO clienteDTO = usuarioMapper.toDTO(savedCliente);
                
                log.info("✅ Cliente creado exitosamente con ID: {}", keycloakId);
                return ResponseEntity.ok(clienteDTO);
                
            } else if ("PROVEEDOR".equalsIgnoreCase(request.getTipoUsuario())) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(keycloakId);
                proveedor.setNombre(request.getNombre());
                proveedor.setEdad(request.getEdad());
                proveedor.setFotoUrl(request.getFotoUrl());
                proveedor.setDescripcion(request.getDescripcion());
                proveedor.setCorreo(request.getCorreo());
                proveedor.setTelefono(request.getTelefono());
                proveedor.setPaginaWeb(request.getPaginaWeb());
                proveedor.setRedesSociales(request.getRedesSociales());
                proveedor.setCalificacionPromedio(request.getCalificacionPromedio());
                
                Proveedor savedProveedor = usuarioRepository.save(proveedor);
                UsuarioDTO proveedorDTO = usuarioMapper.toDTO(savedProveedor);
                
                log.info("✅ Proveedor creado exitosamente con ID: {}", keycloakId);
                return ResponseEntity.ok(proveedorDTO);
            } else {
                return ResponseEntity.badRequest()
                    .body("❌ Tipo de usuario inválido. Debe ser CLIENTE o PROVEEDOR");
            }

        } catch (Exception e) {
            log.error("❌ Error al crear usuario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body("❌ Error al crear usuario: " + e.getMessage());
        }
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