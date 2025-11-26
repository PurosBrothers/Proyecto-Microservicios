package com.microservicios.transaction_ms.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idItem;
    private int cantidad;
    private BigDecimal precioUnitario;
    private LocalDate fechaAgregado;
    private String nombreItem;

    public ItemCarrito() {
    }

    public ItemCarrito(Long id, Long idItem, int cantidad, BigDecimal precioUnitario, LocalDate fechaAgregado,
            String nombreItem) {
        this.id = id;
        this.idItem = idItem;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.fechaAgregado = fechaAgregado;
        this.nombreItem = nombreItem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public LocalDate getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDate fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    public String getNombreItem() {
        return nombreItem;
    }

    public void setNombreItem(String nombreItem) {
        this.nombreItem = nombreItem;
    }

}
