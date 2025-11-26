package com.microservicios.transaction_ms.dtos;

import java.time.LocalDate;
import java.util.List;

public class CarritoCompraDTO {
    private Long id;
    private String UID;
    private LocalDate fechaCreacion;
    private LocalDate fechaUltimaModificacion;
    private Boolean activo;
    private List<ItemCarritoDTO> items;

    public CarritoCompraDTO() {
    }

    public CarritoCompraDTO(Long id, String UID, LocalDate fechaCreacion, LocalDate fechaUltimaModificacion,
            Boolean activo, List<ItemCarritoDTO> items) {
        this.id = id;
        this.UID = UID;
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimaModificacion = fechaUltimaModificacion;
        this.activo = activo;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String uID) {
        UID = uID;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(LocalDate fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<ItemCarritoDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemCarritoDTO> items) {
        this.items = items;
    }
}