package com.microservicios.marketplace_ms.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicios.marketplace_ms.dtos.CalificacionComentarioDTO;
import com.microservicios.marketplace_ms.dtos.ComentarioRespuestaDTO;
import com.microservicios.marketplace_ms.entities.Calificacion;
import com.microservicios.marketplace_ms.entities.Comentario;
import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.repositories.CalificacionRepository;
import com.microservicios.marketplace_ms.repositories.ComentarioRepository;
import com.microservicios.marketplace_ms.repositories.ItemRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/items/{itemId}/reviews")
public class CalificacionComentarioController {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Obtener todas las calificaciones con comentarios de un item
     */
    @GetMapping
    public ResponseEntity<List<Comentario>> getReviewsByItem(@PathVariable Long itemId) {
        // Buscar comentarios padre (sin parent) que pertenezcan al item
        List<Comentario> comentariosPadre = comentarioRepository.findByItemIdAndParentIsNull(itemId);
        return ResponseEntity.ok(comentariosPadre);
    }

    /**
     * Crear calificación + comentario (solo para comentarios padre)
     */
    @PostMapping
    @PreAuthorize("hasRole('TURISTA') or hasRole('PROVEEDOR') or hasAuthority('SCOPE_profile')")
    public ResponseEntity<?> createReview(@PathVariable Long itemId, 
                                        @Valid @RequestBody CalificacionComentarioDTO reviewDTO) {
        
        // Verificar que el item existe
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (!itemOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Item no encontrado");
        }
        Item item = itemOpt.get();

        // Validar que la puntuación esté entre 1 y 5
        if (reviewDTO.getPuntuacion() < 1 || reviewDTO.getPuntuacion() > 5) {
            return ResponseEntity.badRequest().body("La puntuación debe estar entre 1 y 5 estrellas");
        }

        try {
            // Crear comentario padre
            Comentario comentario = new Comentario();
            comentario.setUid(reviewDTO.getUid());
            comentario.setTitulo(reviewDTO.getTitulo());
            comentario.setCuerpo(reviewDTO.getComentario());
            comentario.setLikes(0);
            comentario.setParent(null); // Es comentario padre
            comentario.setItem(item); // Asociar al item
            
            // Guardar comentario primero
            comentario = comentarioRepository.save(comentario);

            // Crear calificación asociada
            Calificacion calificacion = new Calificacion();
            calificacion.setUid(reviewDTO.getUid());
            calificacion.setPuntuacion(reviewDTO.getPuntuacion());
            calificacion.setComentario(comentario);
            calificacion.setFechaCalificacion(LocalDateTime.now());
            calificacion.setItem(item);

            calificacion = calificacionRepository.save(calificacion);

            // Actualizar la relación bidireccional
            comentario.setCalificacion(calificacion);
            comentario = comentarioRepository.save(comentario);

            return ResponseEntity.status(HttpStatus.CREATED).body(comentario);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la calificación: " + e.getMessage());
        }
    }

    /**
     * Responder a un comentario (sin calificación)
     */
    @PostMapping("/{comentarioPadreId}/replies")
    @PreAuthorize("hasRole('TURISTA') or hasRole('PROVEEDOR') or hasAuthority('SCOPE_profile')")
    public ResponseEntity<?> replyToComment(@PathVariable Long itemId,
                                          @PathVariable Long comentarioPadreId,
                                          @Valid @RequestBody ComentarioRespuestaDTO replyDTO) {
        
        // Verificar que el comentario padre existe
        Optional<Comentario> comentarioPadreOpt = comentarioRepository.findById(comentarioPadreId);
        if (!comentarioPadreOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Comentario padre no encontrado");
        }
        
        Comentario comentarioPadre = comentarioPadreOpt.get();
        
        // Verificar que efectivamente es un comentario padre del item correcto
        if (!comentarioPadre.isComentarioPadre() || !comentarioPadre.getItem().getId().equals(itemId)) {
            return ResponseEntity.badRequest().body("Solo se puede responder a comentarios padre del item especificado");
        }

        try {
            // Crear respuesta (sin calificación)
            Comentario respuesta = new Comentario();
            respuesta.setUid(replyDTO.getUid());
            respuesta.setTitulo(replyDTO.getTitulo());
            respuesta.setCuerpo(replyDTO.getCuerpo());
            respuesta.setLikes(0);
            respuesta.setParent(comentarioPadre); // Es respuesta
            respuesta.setItem(null); // Las respuestas no se asocian directamente al item
            respuesta.setCalificacion(null); // Las respuestas no tienen calificación

            respuesta = comentarioRepository.save(respuesta);

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la respuesta: " + e.getMessage());
        }
    }

    /**
     * Obtener estadísticas de calificaciones de un item
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getReviewStats(@PathVariable Long itemId) {
        try {
            List<Calificacion> calificaciones = calificacionRepository.findByItemId(itemId);
            
            if (calificaciones.isEmpty()) {
                return ResponseEntity.ok().body(new ReviewStatsDTO(0, 0.0, 0, 0, 0, 0, 0));
            }

            double promedio = calificaciones.stream()
                    .mapToInt(Calificacion::getPuntuacion)
                    .average()
                    .orElse(0.0);

            int total = calificaciones.size();
            int cinco = (int) calificaciones.stream().filter(c -> c.getPuntuacion() == 5).count();
            int cuatro = (int) calificaciones.stream().filter(c -> c.getPuntuacion() == 4).count();
            int tres = (int) calificaciones.stream().filter(c -> c.getPuntuacion() == 3).count();
            int dos = (int) calificaciones.stream().filter(c -> c.getPuntuacion() == 2).count();
            int uno = (int) calificaciones.stream().filter(c -> c.getPuntuacion() == 1).count();

            ReviewStatsDTO stats = new ReviewStatsDTO(total, promedio, cinco, cuatro, tres, dos, uno);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    /**
     * Eliminar calificación completa (solo el autor)
     */
    @DeleteMapping("/{comentarioId}")
    @PreAuthorize("hasRole('TURISTA') or hasRole('PROVEEDOR') or hasAuthority('SCOPE_profile')")
    public ResponseEntity<?> deleteReview(@PathVariable Long itemId, @PathVariable Long comentarioId) {
        
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(comentarioId);
        if (!comentarioOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Comentario comentario = comentarioOpt.get();
        
        // Verificar que es un comentario padre del item correcto
        if (!comentario.isComentarioPadre() || !comentario.getItem().getId().equals(itemId)) {
            return ResponseEntity.badRequest().body("Solo se pueden eliminar comentarios padre del item especificado");
        }

        try {
            // Al eliminar el comentario padre, se eliminan automáticamente:
            // - La calificación (por cascade)
            // - Las respuestas (por cascade)
            comentarioRepository.delete(comentario);
            return ResponseEntity.ok().body("Calificación eliminada exitosamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la calificación: " + e.getMessage());
        }
    }

    // Clase interna para estadísticas
    public static class ReviewStatsDTO {
        private int totalReviews;
        private double promedioCalificacion;
        private int cincoEstrellas;
        private int cuatroEstrellas;
        private int tresEstrellas;
        private int dosEstrellas;
        private int unaEstrella;

        public ReviewStatsDTO(int totalReviews, double promedioCalificacion, 
                             int cincoEstrellas, int cuatroEstrellas, int tresEstrellas, 
                             int dosEstrellas, int unaEstrella) {
            this.totalReviews = totalReviews;
            this.promedioCalificacion = Math.round(promedioCalificacion * 100.0) / 100.0; // 2 decimales
            this.cincoEstrellas = cincoEstrellas;
            this.cuatroEstrellas = cuatroEstrellas;
            this.tresEstrellas = tresEstrellas;
            this.dosEstrellas = dosEstrellas;
            this.unaEstrella = unaEstrella;
        }

        // Getters y setters
        public int getTotalReviews() { return totalReviews; }
        public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }
        
        public double getPromedioCalificacion() { return promedioCalificacion; }
        public void setPromedioCalificacion(double promedioCalificacion) { this.promedioCalificacion = promedioCalificacion; }
        
        public int getCincoEstrellas() { return cincoEstrellas; }
        public void setCincoEstrellas(int cincoEstrellas) { this.cincoEstrellas = cincoEstrellas; }
        
        public int getCuatroEstrellas() { return cuatroEstrellas; }
        public void setCuatroEstrellas(int cuatroEstrellas) { this.cuatroEstrellas = cuatroEstrellas; }
        
        public int getTresEstrellas() { return tresEstrellas; }
        public void setTresEstrellas(int tresEstrellas) { this.tresEstrellas = tresEstrellas; }
        
        public int getDosEstrellas() { return dosEstrellas; }
        public void setDosEstrellas(int dosEstrellas) { this.dosEstrellas = dosEstrellas; }
        
        public int getUnaEstrella() { return unaEstrella; }
        public void setUnaEstrella(int unaEstrella) { this.unaEstrella = unaEstrella; }
    }
}