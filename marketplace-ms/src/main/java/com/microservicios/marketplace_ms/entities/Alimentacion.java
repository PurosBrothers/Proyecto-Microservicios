package com.microservicios.marketplace_ms.entities;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("Alimentacion")
public class Alimentacion extends Clasificacion {

    private LocalTime horaInicio;
    private LocalTime horaFinal;
    private String tipoComida;
    private String menuIncluido;
    private BigDecimal latitud;
    private BigDecimal longitud;

    @OneToMany(mappedBy = "alimentacion", cascade = CascadeType.ALL)
    private List<RestriccionesDieteticas> restriccionesDieteticas = new ArrayList<>();

    // Getters and Setters for specific fields
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

    public List<RestriccionesDieteticas> getRestriccionesDieteticas() {
        return restriccionesDieteticas;
    }

    public void setRestriccionesDieteticas(List<RestriccionesDieteticas> restriccionesDieteticas) {
        this.restriccionesDieteticas = restriccionesDieteticas;
    }

}