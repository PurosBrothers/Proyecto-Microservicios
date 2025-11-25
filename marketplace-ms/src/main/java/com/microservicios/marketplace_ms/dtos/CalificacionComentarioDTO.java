package com.microservicios.marketplace_ms.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para crear calificación + comentario (comentarios padre)
 */
public class CalificacionComentarioDTO {
    
    @NotNull(message = "El UID del usuario es obligatorio")
    private Long uid;
    
    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1 estrella")
    @Max(value = 5, message = "La puntuación máxima es 5 estrellas")
    private Integer puntuacion;
    
    @NotBlank(message = "El título del comentario es obligatorio")
    private String titulo;
    
    private String comentario; // Opcional

    // Constructores
    public CalificacionComentarioDTO() {}

    public CalificacionComentarioDTO(Long uid, Integer puntuacion, String titulo, String comentario) {
        this.uid = uid;
        this.puntuacion = puntuacion;
        this.titulo = titulo;
        this.comentario = comentario;
    }

    // Getters y Setters
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}