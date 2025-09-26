package com.microservicios.marketplace_ms.dtos;

public class ItemResponseDTO {
    private ItemDTO item;
    private Object clasificacionData;

    // Getters and Setters
    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public Object getClasificacionData() {
        return clasificacionData;
    }

    public void setClasificacionData(Object clasificacionData) {
        this.clasificacionData = clasificacionData;
    }
}