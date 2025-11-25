package com.microservicios.marketplace_ms.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicios.marketplace_ms.entities.Comentario;
import com.microservicios.marketplace_ms.repositories.ComentarioRepository;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    /**
     * Dar/quitar like a un comentario
     */
    @PostMapping("/{comentarioId}/like")
    @PreAuthorize("hasRole('TURISTA') or hasRole('PROVEEDOR') or hasAuthority('SCOPE_profile')")
    public ResponseEntity<?> toggleLike(@PathVariable Long comentarioId) {
        
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
            
            return ResponseEntity.ok().body("Like agregado. Total: " + comentario.getLikes());
            
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
}