package com.microservicios.marketplace_ms.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.microservicios.marketplace_ms.entities.Clasificacion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ItemDTO {
    private Long id;
    @NotNull(message = "La clasificación es obligatoria")
    private Clasificacion clasificacion;
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    private LocalDate fechaPublicacion;
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    private Integer visualizaciones;
    private Long calificacionPromedio;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getVisualizaciones() {
        return visualizaciones;
    }

    public void setVisualizaciones(Integer visualizaciones) {
        this.visualizaciones = visualizaciones;
    }

    public Long getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(Long calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }
}
