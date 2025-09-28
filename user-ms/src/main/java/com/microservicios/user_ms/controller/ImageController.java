package com.microservicios.user_ms.controller;

import com.microservicios.user_ms.entity.Usuario;
import com.microservicios.user_ms.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para manejar imágenes de perfil almacenadas en H2
 * 
 * Endpoints:
 * - POST /users/{userId}/image - Subir imagen de perfil
 * - GET /users/{userId}/image - Obtener imagen de perfil
 * - GET /images/{userId} - Endpoint alternativo para obtener imagen
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ImageController {

    private final UsuarioRepository usuarioRepository;

    /**
     * Subir imagen de perfil para un usuario
     */
    @PostMapping("/users/{userId}/image")
    public ResponseEntity<Map<String, String>> uploadUserImage(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        
        try {
            // Validar que es una imagen
            if (!isImageFile(file)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El archivo debe ser una imagen (JPG, PNG, GIF, WEBP)"));
            }

            // Buscar usuario
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Usuario usuario = usuarioOpt.get();

            // Guardar datos de la imagen
            usuario.setFotoData(file.getBytes());
            usuario.setFotoTipo(file.getContentType());
            usuario.setFotoNombre(file.getOriginalFilename());
            usuario.setFotoUrl("http://localhost:8083/images/" + userId); // URL para acceder a la imagen

            usuarioRepository.save(usuario);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Imagen subida correctamente");
            response.put("imageUrl", usuario.getFotoUrl());
            response.put("fileName", file.getOriginalFilename());
            response.put("size", String.valueOf(file.getSize()));
            response.put("contentType", file.getContentType());

            log.info("Imagen subida para usuario {}: {} bytes", userId, file.getSize());
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("Error al subir imagen para usuario {}: {}", userId, e.getMessage());
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error al procesar la imagen: " + e.getMessage()));
        }
    }

    /**
     * Obtener imagen de perfil de un usuario
     */
    @GetMapping("/users/{userId}/image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable String userId) {
        return getImageResponse(userId);
    }

    /**
     * Endpoint alternativo para obtener imagen (más amigable para frontends)
     */
    @GetMapping("/images/{userId}")
    public ResponseEntity<byte[]> getImage(@PathVariable String userId) {
        return getImageResponse(userId);
    }

    /**
     * Eliminar imagen de perfil de un usuario
     */
    @DeleteMapping("/users/{userId}/image")
    public ResponseEntity<Map<String, String>> deleteUserImage(@PathVariable String userId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setFotoData(null);
        usuario.setFotoTipo(null);
        usuario.setFotoNombre(null);
        usuario.setFotoUrl(null);

        usuarioRepository.save(usuario);

        log.info("Imagen eliminada para usuario {}", userId);
        return ResponseEntity.ok(Map.of("message", "Imagen eliminada correctamente"));
    }

    /**
     * Método privado para construir la respuesta de imagen
     */
    private ResponseEntity<byte[]> getImageResponse(String userId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();
        
        if (usuario.getFotoData() == null || usuario.getFotoData().length == 0) {
            // Devolver imagen por defecto o 404
            return ResponseEntity.notFound().build();
        }

        // Determinar content type
        String contentType = usuario.getFotoTipo();
        if (contentType == null) {
            contentType = "image/jpeg"; // default
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + usuario.getFotoNombre() + "\"")
            .header(HttpHeaders.CACHE_CONTROL, "max-age=3600") // Cache por 1 hora
            .body(usuario.getFotoData());
    }

    /**
     * Validar que el archivo es una imagen
     */
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
            contentType.equals("image/jpeg") ||
            contentType.equals("image/jpg") ||
            contentType.equals("image/png") ||
            contentType.equals("image/gif") ||
            contentType.equals("image/webp")
        );
    }
}