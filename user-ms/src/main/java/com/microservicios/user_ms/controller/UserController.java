package com.microservicios.user_ms.controller;


import com.microservicios.user_ms.dto.UsuarioDTO;
import com.microservicios.user_ms.service.UsuarioService;
import com.microservicios.user_ms.enums.TipoUsuario;
import com.microservicios.user_ms.entity.Cliente;
import com.microservicios.user_ms.entity.Proveedor;
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

import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;




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
     * Sincroniza cambios con Keycloak automáticamente
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_profile') or hasAuthority('SCOPE_email')")
    public ResponseEntity<?> updateUser(@PathVariable String id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            log.info("🔄 Actualizando usuario ID: {} - Email: {}", id, usuarioDTO.getCorreo());
            
            UsuarioDTO updated = usuarioService.updateUsuario(id, usuarioDTO);
            if (updated != null) {
                log.info("✅ Usuario actualizado exitosamente: {}", updated.getCorreo());
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Usuario actualizado exitosamente en BD y Keycloak");
                response.put("usuario", updated);
                return ResponseEntity.ok(response);
            }
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuario no encontrado con ID: " + id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("❌ Error al actualizar usuario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al actualizar usuario: " + e.getMessage()));
        }
    }

    /**
     * Eliminar usuario (requiere autenticación JWT)
     * Elimina el usuario tanto de BD como de Keycloak
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_profile') or hasAuthority('SCOPE_email')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            log.info("🗑️ Eliminando usuario ID: {}", id);
            
            UsuarioDTO deleted = usuarioService.deleteUsuario(id);
            if (deleted != null) {
                log.info("✅ Usuario eliminado exitosamente: {}", deleted.getCorreo());
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Usuario eliminado exitosamente de BD y Keycloak");
                response.put("usuarioEliminado", deleted);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("❌ Error al eliminar usuario: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al eliminar usuario: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Endpoint de información del sistema (para debug)
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("totalUsers", usuarioRepository.count());
        info.put("keycloakAvailable", false); // Ahora manejado por el gateway
        info.put("message", "Usuario gestionado por gateway - UUIDs generados por Keycloak");
        return ResponseEntity.ok(info);
    }

    /**
     * Endpoint interno para crear usuario desde el gateway
     * Este endpoint es usado por el gateway después de crear el usuario en Keycloak
     */
    @PostMapping("/internal/create")
    public ResponseEntity<Map<String, Object>> createUserInternal(@RequestBody InternalUserCreationRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("📡 Creando usuario interno desde gateway: {}", request.getCorreo());

            // Verificar que el usuario no exista ya
            if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
                response.put("error", "El usuario ya existe en la base de datos");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear usuario según tipo
            com.microservicios.user_ms.entity.Usuario usuario = crearUsuarioDesdeRequest(request);

            // Guardar en base de datos
            usuarioRepository.save(usuario);

            response.put("message", "Usuario creado exitosamente en base de datos");
            response.put("id", usuario.getId());
            response.put("correo", usuario.getCorreo());

            log.info("✅ Usuario interno creado: {}", request.getCorreo());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Error creando usuario interno: {}", e.getMessage());
            response.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Crea usuario desde request interno
     */
    private com.microservicios.user_ms.entity.Usuario crearUsuarioDesdeRequest(InternalUserCreationRequest request) {
        com.microservicios.user_ms.entity.Usuario usuario;

        TipoUsuario tipoUsuario = TipoUsuario.valueOf(request.getTipoUsuario());

        switch (tipoUsuario) {
            case CLIENTE:
                Cliente cliente = new Cliente();
                usuario = cliente;
                break;

            case PROVEEDOR:
                Proveedor proveedor = new Proveedor();
                usuario = proveedor;
                break;

            default:
                throw new IllegalArgumentException("Tipo de usuario no válido: " + request.getTipoUsuario());
        }

        // Configurar campos
        usuario.setId(request.getKeycloakUserId());
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setEdad(request.getEdad());
        usuario.setDescripcion(request.getDescripcion());
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setTipoUsuario(tipoUsuario);

        return usuario;
    }

    /**
     * DTO para request interno de creación de usuario
     */
    public static class InternalUserCreationRequest {
        private String keycloakUserId;
        private String nombre;
        private String correo;
        private Integer edad;
        private String descripcion;
        private String telefono;
        private String direccion;
        private String tipoUsuario;

        // Getters y setters
        public String getKeycloakUserId() { return keycloakUserId; }
        public void setKeycloakUserId(String keycloakUserId) { this.keycloakUserId = keycloakUserId; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public Integer getEdad() { return edad; }
        public void setEdad(Integer edad) { this.edad = edad; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }

        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }

        public String getTipoUsuario() { return tipoUsuario; }
        public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    }



}