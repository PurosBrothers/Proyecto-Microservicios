package com.microservicios.transaction_ms.dtos;

public class UpdateItemMessageDTO {
    private Long itemId;
    private Integer cantidadVendida;
    private String tipoCambio; // "stock", "fechas", "cupo"

    public UpdateItemMessageDTO() {
    }

    public UpdateItemMessageDTO(Long itemId, Integer cantidadVendida, String tipoCambio) {
        this.itemId = itemId;
        this.cantidadVendida = cantidadVendida;
        this.tipoCambio = tipoCambio;
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(Integer cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public String getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(String tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
}