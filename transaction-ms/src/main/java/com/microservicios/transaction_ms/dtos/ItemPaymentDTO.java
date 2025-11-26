package com.microservicios.transaction_ms.dtos;

import java.math.BigDecimal;

public class ItemPaymentDTO {
    private Long idItem;
    private int cantidad;
    private BigDecimal precioUnitario;

    public ItemPaymentDTO() {
    }

    public ItemPaymentDTO(Long idItem, int cantidad, BigDecimal precioUnitario) {
        this.idItem = idItem;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Getters and Setters
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
}