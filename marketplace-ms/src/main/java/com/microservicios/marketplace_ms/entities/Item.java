package com.microservicios.marketplace_ms.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clasificacion_id")
    private Clasificacion clasificacion;

    private String titulo;
    private String descripcion;
    private LocalDate fechaPublicacion;
    private Integer stock;
    private Integer visualizaciones;
    private Long calificacionPromedio;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemFoto> fotos = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemVideo> videos = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemLink> links = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Calificacion> calificaciones = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ServiciosIncluidos> serviciosIncluidos = new ArrayList<>();

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

    public String getLugarInicio() {
        return clasificacion != null ? clasificacion.getLugarInicio() : null;
    }

    public void setLugarInicio(String lugarInicio) {
        if (clasificacion != null) {
            clasificacion.setLugarInicio(lugarInicio);
        }
    }

    public BigDecimal getPrecio() {
        return clasificacion != null ? clasificacion.getPrecio() : null;
    }

    public void setPrecio(BigDecimal precio) {
        if (clasificacion != null) {
            clasificacion.setPrecio(precio);
        }
    }

    public LocalDateTime getFechaDisponibilidadInicio() {
        return clasificacion != null ? clasificacion.getFechaDisponibilidadInicio() : null;
    }

    public void setFechaDisponibilidadInicio(LocalDateTime fechaDisponibilidadInicio) {
        if (clasificacion != null) {
            clasificacion.setFechaDisponibilidadInicio(fechaDisponibilidadInicio);
        }
    }

    public LocalDateTime getFechaDisponibilidadFin() {
        return clasificacion != null ? clasificacion.getFechaDisponibilidadFin() : null;
    }

    public void setFechaDisponibilidadFin(LocalDateTime fechaDisponibilidadFin) {
        if (clasificacion != null) {
            clasificacion.setFechaDisponibilidadFin(fechaDisponibilidadFin);
        }
    }

    public Integer getCapacidadMaxima() {
        return clasificacion != null ? clasificacion.getCapacidadMaxima() : null;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        if (clasificacion != null) {
            clasificacion.setCapacidadMaxima(capacidadMaxima);
        }
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

    public List<ItemTag> getTags() {
        return tags;
    }

    public void setTags(List<ItemTag> tags) {
        this.tags = tags;
    }

    public List<ItemFoto> getFotos() {
        return fotos;
    }

    public void setFotos(List<ItemFoto> fotos) {
        this.fotos = fotos;
    }

    public List<ItemVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<ItemVideo> videos) {
        this.videos = videos;
    }

    public List<ItemLink> getLinks() {
        return links;
    }

    public void setLinks(List<ItemLink> links) {
        this.links = links;
    }

    public List<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public List<ServiciosIncluidos> getServiciosIncluidos() {
        return serviciosIncluidos;
    }

    public void setServiciosIncluidos(List<ServiciosIncluidos> serviciosIncluidos) {
        this.serviciosIncluidos = serviciosIncluidos;
    }

}
