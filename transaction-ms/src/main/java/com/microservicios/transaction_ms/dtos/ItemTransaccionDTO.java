package com.microservicios.transaction_ms.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ItemTransaccionDTO {
    private Long id;
    private Long reservaId;
    private Long ofertaId;
    private int cantidad;
    private BigDecimal precioUnitario;
    private LocalDate fechaIncioServicio;
    private LocalDate fechaFinServicio;

    public ItemTransaccionDTO() {
    }

    public ItemTransaccionDTO(Long id, Long reservaId, Long ofertaId, int cantidad, BigDecimal precioUnitario,
            LocalDate fechaIncioServicio, LocalDate fechaFinServicio) {
        this.id = id;
        this.reservaId = reservaId;
        this.ofertaId = ofertaId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.fechaIncioServicio = fechaIncioServicio;
        this.fechaFinServicio = fechaFinServicio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
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

    public LocalDate getFechaIncioServicio() {
        return fechaIncioServicio;
    }

    public void setFechaIncioServicio(LocalDate fechaIncioServicio) {
        this.fechaIncioServicio = fechaIncioServicio;
    }

    public LocalDate getFechaFinServicio() {
        return fechaFinServicio;
    }

    public void setFechaFinServicio(LocalDate fechaFinServicio) {
        this.fechaFinServicio = fechaFinServicio;
    }
}