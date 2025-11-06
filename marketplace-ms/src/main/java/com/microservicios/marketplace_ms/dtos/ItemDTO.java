package com.microservicios.marketplace_ms.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ItemDTO {
    private Long id;
    
    @NotNull(message = "El ID de clasificación es obligatorio")
    private Long clasificacionId;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String titulo;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    private String descripcion;
    
    private LocalDate fechaPublicacion;
    
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    // Campos heredados de clasificación (solo lectura en responses)
    private String lugarInicio;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
    
    private LocalDateTime fechaDisponibilidadInicio;
    private LocalDateTime fechaDisponibilidadFin;
    
    @Min(value = 1, message = "La capacidad máxima debe ser al menos 1")
    private Integer capacidadMaxima;
    
    // Campos de solo lectura (responses)
    private Integer visualizaciones;
    private Long calificacionPromedio;
    private String usuarioId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClasificacionId() {
        return clasificacionId;
    }

    public void setClasificacionId(Long clasificacionId) {
        this.clasificacionId = clasificacionId;
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

    public String getLugarInicio() {
        return lugarInicio;
    }

    public void setLugarInicio(String lugarInicio) {
        this.lugarInicio = lugarInicio;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public LocalDateTime getFechaDisponibilidadInicio() {
        return fechaDisponibilidadInicio;
    }

    public void setFechaDisponibilidadInicio(LocalDateTime fechaDisponibilidadInicio) {
        this.fechaDisponibilidadInicio = fechaDisponibilidadInicio;
    }

    public LocalDateTime getFechaDisponibilidadFin() {
        return fechaDisponibilidadFin;
    }

    public void setFechaDisponibilidadFin(LocalDateTime fechaDisponibilidadFin) {
        this.fechaDisponibilidadFin = fechaDisponibilidadFin;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
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

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
