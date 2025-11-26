package com.microservicios.transaction_ms.dtos;

import java.math.BigDecimal;

public class AddToCartMessageDTO {
    private String uid;
    private Long idItem;
    private int cantidad;
    private BigDecimal precioUnitario;
    private String tipoClasificacion;
    private String nombreItem;

    public AddToCartMessageDTO() {
    }

    public AddToCartMessageDTO(String uid, Long idItem, int cantidad, BigDecimal precioUnitario,
            String tipoClasificacion, String nombreItem) {
        this.uid = uid;
        this.idItem = idItem;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tipoClasificacion = tipoClasificacion;
        this.nombreItem = nombreItem;
    }

    // Getters and Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getTipoClasificacion() {
        return tipoClasificacion;
    }

    public void setTipoClasificacion(String tipoClasificacion) {
        this.tipoClasificacion = tipoClasificacion;
    }

    public String getNombreItem() {
        return nombreItem;
    }

    public void setNombreItem(String nombreItem) {
        this.nombreItem = nombreItem;
    }
}