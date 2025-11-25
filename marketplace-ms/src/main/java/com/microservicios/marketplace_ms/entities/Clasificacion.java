package com.microservicios.marketplace_ms.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Embedded;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Alojamiento.class, name = "Alojamiento"),
        @JsonSubTypes.Type(value = Alimentacion.class, name = "Alimentacion"),
        @JsonSubTypes.Type(value = Transporte.class, name = "Transporte"),
        @JsonSubTypes.Type(value = PaseosEcologicos.class, name = "PaseosEcologicos")
})
public abstract class Clasificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "tipo", insertable = false, updatable = false)
    protected String tipo;

    protected String lugarInicio;
    protected BigDecimal precio;
    protected LocalDateTime fechaDisponibilidadInicio;
    protected LocalDateTime fechaDisponibilidadFin;
    protected Integer capacidadMaxima;
    protected String paisDestino;
    protected String flag;
    protected Long population;
    protected Double gini;
    protected String fifa;

    @Embedded
    protected Maps maps;

    @Column(name = "usuario_id", nullable = false)
    protected String usuarioId;

    @ManyToMany
    protected List<RequisitosEspeciales> requisitosEspeciales = new ArrayList<>();

    public Clasificacion() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public List<RequisitosEspeciales> getRequisitosEspeciales() {
        return requisitosEspeciales;
    }

    public void setRequisitosEspeciales(List<RequisitosEspeciales> requisitosEspeciales) {
        this.requisitosEspeciales = requisitosEspeciales;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getPaisDestino() {
        return paisDestino;
    }

    public void setPaisDestino(String paisDestino) {
        this.paisDestino = paisDestino;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public Double getGini() {
        return gini;
    }

    public void setGini(Double gini) {
        this.gini = gini;
    }

    public String getFifa() {
        return fifa;
    }

    public void setFifa(String fifa) {
        this.fifa = fifa;
    }

    public Maps getMaps() {
        return maps;
    }

    public void setMaps(Maps maps) {
        this.maps = maps;
    }
}