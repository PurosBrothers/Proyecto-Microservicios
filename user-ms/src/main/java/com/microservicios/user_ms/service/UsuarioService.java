package com.microservicios.user_ms.service;

import com.microservicios.user_ms.dto.UsuarioDTO;
import com.microservicios.user_ms.entity.Usuario;
import com.microservicios.user_ms.mapper.UsuarioMapper;
import com.microservicios.user_ms.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final KeycloakService keycloakService;

    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario.setId(getCurrentUserId());
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(savedUsuario);
    }

    public UsuarioDTO getUsuario(String id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(usuarioMapper::toDTO).orElse(null);
    }

    @Transactional
    public UsuarioDTO updateUsuario(String id, UsuarioDTO usuarioDTO) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        if (existingUsuario.isPresent()) {
            Usuario usuario = existingUsuario.get();
            
            // Guardar valores anteriores para Keycloak
            String oldEmail = usuario.getCorreo();
            String oldName = usuario.getNombre();
            String oldLastName = usuario.getApellido();
            
            // Update fields
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setApellido(usuarioDTO.getApellido());
            usuario.setEdad(usuarioDTO.getEdad());
            usuario.setFotoUrl(usuarioDTO.getFotoUrl());
            usuario.setDescripcion(usuarioDTO.getDescripcion());
            usuario.setCorreo(usuarioDTO.getCorreo());
            
            // Handle specific fields based on type
            if (usuario instanceof com.microservicios.user_ms.entity.Cliente) {
                com.microservicios.user_ms.entity.Cliente cliente = (com.microservicios.user_ms.entity.Cliente) usuario;
                cliente.setDireccion(usuarioDTO.getDireccion());
                cliente.setTelefono(usuarioDTO.getTelefono());
            } else if (usuario instanceof com.microservicios.user_ms.entity.Proveedor) {
                com.microservicios.user_ms.entity.Proveedor proveedor = (com.microservicios.user_ms.entity.Proveedor) usuario;
                proveedor.setTelefono(usuarioDTO.getTelefono());
                proveedor.setPaginaWeb(usuarioDTO.getPaginaWeb());
                proveedor.setRedesSociales(usuarioDTO.getRedesSociales());
                proveedor.setCalificacionPromedio(usuarioDTO.getCalificacionPromedio());
            }
            
            // Actualizar en base de datos primero
            Usuario savedUsuario = usuarioRepository.save(usuario);
            
            // Sincronizar con Keycloak si cambió el email, nombre o apellido
            if (!oldEmail.equals(usuarioDTO.getCorreo()) || 
                !oldName.equals(usuarioDTO.getNombre()) || 
                !oldLastName.equals(usuarioDTO.getApellido())) {
                
                boolean keycloakUpdated = keycloakService.updateKeycloakUser(
                    id, 
                    usuarioDTO.getCorreo(),
                    usuarioDTO.getNombre(),
                    usuarioDTO.getApellido()
                );
                
                if (!keycloakUpdated) {
                    log.warn("⚠️ Usuario actualizado en BD pero no se pudo sincronizar con Keycloak: {}", id);
                } else {
                    log.info("✅ Usuario actualizado en BD y Keycloak: {}", usuarioDTO.getCorreo());
                }
            }
            
            return usuarioMapper.toDTO(savedUsuario);
        }
        return null;
    }

    @Transactional
    public UsuarioDTO deleteUsuario(String id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            UsuarioDTO dto = usuarioMapper.toDTO(usuario.get());
            
            // Eliminar de base de datos primero
            usuarioRepository.deleteById(id);
            log.info("✅ Usuario eliminado de BD: {}", dto.getCorreo());
            
            // Sincronizar eliminación con Keycloak
            boolean keycloakDeleted = keycloakService.deleteKeyCloakUser(id);
            
            if (!keycloakDeleted) {
                log.warn("⚠️ Usuario eliminado de BD pero no se pudo eliminar de Keycloak: {}", id);
            } else {
                log.info("✅ Usuario eliminado de BD y Keycloak: {}", dto.getCorreo());
            }
            
            return dto;
        }
        return null;
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject();
        }
        throw new RuntimeException("Usuario no autenticado");
    }
}