package com.microservicios.marketplace_ms.dtos;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AlimentacionDTO extends ClasificacionDTO {
    
    private LocalTime horaInicio;
    private LocalTime horaFinal;
    private String tipoComida;
    private String menuIncluido;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private List<RestriccionesDieteticasDTO> restriccionesDieteticas = new ArrayList<>();

    // Constructors
    public AlimentacionDTO() {
        super();
    }

    // Getters and Setters
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public String getMenuIncluido() {
        return menuIncluido;
    }

    public void setMenuIncluido(String menuIncluido) {
        this.menuIncluido = menuIncluido;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public List<RestriccionesDieteticasDTO> getRestriccionesDieteticas() {
        return restriccionesDieteticas;
    }

    public void setRestriccionesDieteticas(List<RestriccionesDieteticasDTO> restriccionesDieteticas) {
        this.restriccionesDieteticas = restriccionesDieteticas;
    }
}