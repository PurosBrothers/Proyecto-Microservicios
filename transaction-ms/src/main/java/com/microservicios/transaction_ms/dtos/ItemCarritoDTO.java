package com.microservicios.transaction_ms.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ItemCarritoDTO {
    private Long id;
    private Long idItem;
    private int cantidad;
    private BigDecimal precioUnitario;
    private LocalDate fechaAgregado;

    public ItemCarritoDTO() {
    }

    public ItemCarritoDTO(Long id, Long idItem, int cantidad, BigDecimal precioUnitario, LocalDate fechaAgregado) {
        this.id = id;
        this.idItem = idItem;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.fechaAgregado = fechaAgregado;
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
}