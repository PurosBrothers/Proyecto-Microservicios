package com.microservicios.marketplace_ms.dtos;

import jakarta.validation.constraints.NotBlank;

public class PreguntaFrecuenteDTO {
    private Long id;

    @NotBlank(message = "La pregunta es obligatoria")
    private String pregunta;

    private Long itemId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}