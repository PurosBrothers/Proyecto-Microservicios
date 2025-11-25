package com.microservicios.marketplace_ms.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para responder a comentarios (sin calificación)
 */
public class ComentarioRespuestaDTO {
    
    @NotNull(message = "El UID del usuario es obligatorio")
    private Long uid;
    
    private String titulo; // Opcional para respuestas
    
    @NotBlank(message = "El contenido de la respuesta es obligatorio")
    private String cuerpo;

    // Constructores
    public ComentarioRespuestaDTO() {}

    public ComentarioRespuestaDTO(Long uid, String titulo, String cuerpo) {
        this.uid = uid;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    // Getters y Setters
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }
}