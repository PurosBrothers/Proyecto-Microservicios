package com.microservicios.marketplace_ms.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class Alojamiento extends Clasificacion {

    private LocalDateTime fechaCheckin;
    private LocalDateTime fechaCheckout;
    private String tipoInmueble;
    private Integer numeroBanos;
    private Integer numeroHabitaciones;
    private BigDecimal lat;
    private BigDecimal lng;

    @ManyToMany
    private List<RequisitosEspeciales> requisitosEspeciales = new ArrayList<>();

    // Getters and Setters
    public LocalDateTime getFechaCheckin() {
        return fechaCheckin;
    }

    public void setFechaCheckin(LocalDateTime fechaCheckin) {
        this.fechaCheckin = fechaCheckin;
    }

    public LocalDateTime getFechaCheckout() {
        return fechaCheckout;
    }

    public void setFechaCheckout(LocalDateTime fechaCheckout) {
        this.fechaCheckout = fechaCheckout;
    }

    public String getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(String tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public Integer getNumeroBanos() {
        return numeroBanos;
    }

    public void setNumeroBanos(Integer numeroBanos) {
        this.numeroBanos = numeroBanos;
    }

    public Integer getNumeroHabitaciones() {
        return numeroHabitaciones;
    }

    public void setNumeroHabitaciones(Integer numeroHabitaciones) {
        this.numeroHabitaciones = numeroHabitaciones;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public List<RequisitosEspeciales> getRequisitosEspeciales() {
        return requisitosEspeciales;
    }

    public void setRequisitosEspeciales(List<RequisitosEspeciales> requisitosEspeciales) {
        this.requisitosEspeciales = requisitosEspeciales;
    }

}