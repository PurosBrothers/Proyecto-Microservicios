package com.microservicios.marketplace_ms.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Alojamiento")
public class Alojamiento extends Clasificacion {

    private LocalDateTime fechaCheckin;
    private LocalDateTime fechaCheckout;
    private String tipoInmueble;
    private Integer numeroBanos;
    private Integer numeroHabitaciones;
    private BigDecimal lat;
    private BigDecimal lng;
    private String direccion;
    private Double temperaturaActual;
    private Double viento;
    private Integer codigoClima;
    private Double lluvia;
    private Double precipitacion;
    private Integer probabilidadPrecipitacion;

    // Getters and Setters for specific fields
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getTemperaturaActual() {
        return temperaturaActual;
    }

    public void setTemperaturaActual(Double temperaturaActual) {
        this.temperaturaActual = temperaturaActual;
    }

    public Double getViento() {
        return viento;
    }

    public void setViento(Double viento) {
        this.viento = viento;
    }

    public Integer getCodigoClima() {
        return codigoClima;
    }

    public void setCodigoClima(Integer codigoClima) {
        this.codigoClima = codigoClima;
    }

    public Double getLluvia() {
        return lluvia;
    }

    public void setLluvia(Double lluvia) {
        this.lluvia = lluvia;
    }

    public Double getPrecipitacion() {
        return precipitacion;
    }

    public void setPrecipitacion(Double precipitacion) {
        this.precipitacion = precipitacion;
    }

    public Integer getProbabilidadPrecipitacion() {
        return probabilidadPrecipitacion;
    }

    public void setProbabilidadPrecipitacion(Integer probabilidadPrecipitacion) {
        this.probabilidadPrecipitacion = probabilidadPrecipitacion;
    }

}