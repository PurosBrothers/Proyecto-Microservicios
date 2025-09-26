package com.microservicios.marketplace_ms.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClasificacionDTO {
    private Long id;
    private String lugarInicio;
    private BigDecimal precio;
    private LocalDateTime fechaDisponibilidadInicio;
    private LocalDateTime fechaDisponibilidadFin;
    private Integer capacidadMaxima;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
