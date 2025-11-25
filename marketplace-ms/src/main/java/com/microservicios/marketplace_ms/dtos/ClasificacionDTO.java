package com.microservicios.marketplace_ms.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlojamientoDTO.class, name = "Alojamiento"),
        @JsonSubTypes.Type(value = AlimentacionDTO.class, name = "Alimentacion"),
        @JsonSubTypes.Type(value = TransporteDTO.class, name = "Transporte"),
        @JsonSubTypes.Type(value = PaseosEcologicosDTO.class, name = "PaseosEcologicos")
})
public abstract class ClasificacionDTO {
    private Long id;
    private String tipo;
    private String lugarInicio;
    private BigDecimal precio;
    private LocalDateTime fechaDisponibilidadInicio;
    private LocalDateTime fechaDisponibilidadFin;
    private Integer capacidadMaxima;
    private String usuarioId;
    private String paisDestino;
    private String flag;
    private Long population;
    private Double gini;
    private String fifa;
    private MapsDTO maps;
    private List<RequisitosEspecialesDTO> requisitosEspeciales = new ArrayList<>();

    // Constructors
    public ClasificacionDTO() {
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

    public List<RequisitosEspecialesDTO> getRequisitosEspeciales() {
        return requisitosEspeciales;
    }

    public void setRequisitosEspeciales(List<RequisitosEspecialesDTO> requisitosEspeciales) {
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

    public MapsDTO getMaps() {
        return maps;
    }

    public void setMaps(MapsDTO maps) {
        this.maps = maps;
    }
}
