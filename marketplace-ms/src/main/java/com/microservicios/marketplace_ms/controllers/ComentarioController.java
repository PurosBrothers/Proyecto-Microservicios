package com.microservicios.marketplace_ms.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicios.marketplace_ms.entities.Comentario;
import com.microservicios.marketplace_ms.repositories.ComentarioRepository;
import com.microservicios.marketplace_ms.utils.JwtTokenUtil;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Dar/quitar like a un comentario
     */
    @PostMapping("/{comentarioId}/like")
    @PreAuthorize("hasRole('TURISTA') or hasRole('PROVEEDOR') or hasAuthority('SCOPE_profile')")
    public ResponseEntity<?> toggleLike(@PathVariable Long comentarioId) {
        
        // Extraer UID del token JWT para validar autenticación
        String userUid = jwtTokenUtil.extractUidFromToken();
        if (userUid == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token JWT inválido o expirado");
        }
        
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(comentarioId);
        if (!comentarioOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Comentario comentario = comentarioOpt.get();
        
        try {
            // Incrementar likes (en una implementación real necesitarías
            // una tabla de likes para evitar likes duplicados del mismo usuario)
            comentario.setLikes(comentario.getLikes() + 1);
            comentario = comentarioRepository.save(comentario);
            
            return ResponseEntity.ok().body("Like agregado por usuario " + userUid + ". Total: " + comentario.getLikes());
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar like: " + e.getMessage());
        }
    }

    /**
     * Obtener comentario con sus respuestas
     */
    @GetMapping("/{comentarioId}")
    public ResponseEntity<?> getComentarioWithReplies(@PathVariable Long comentarioId) {
        
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(comentarioId);
        if (!comentarioOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(comentarioOpt.get());
    }

    /**
     * Endpoint de prueba para validar extracción de información del token JWT
     */
    @GetMapping("/debug/token-info")
    @PreAuthorize("hasRole('TURISTA') or hasRole('PROVEEDOR') or hasAuthority('SCOPE_profile')")
    public ResponseEntity<?> getTokenInfo() {
        try {
            String uid = jwtTokenUtil.extractUidFromToken();
            String email = jwtTokenUtil.extractEmailFromToken();
            
            var tokenInfo = new java.util.HashMap<String, Object>();
            tokenInfo.put("uid", uid);
            tokenInfo.put("email", email);
            tokenInfo.put("message", "Token extraído exitosamente");
            
            return ResponseEntity.ok(tokenInfo);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al extraer información del token: " + e.getMessage());
        }
    }
}