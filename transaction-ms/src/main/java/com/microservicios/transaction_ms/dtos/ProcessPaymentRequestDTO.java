package com.microservicios.transaction_ms.dtos;

import java.util.List;

public class ProcessPaymentRequestDTO {
    private String uid;
    private List<Long> itemIds;

    public ProcessPaymentRequestDTO() {
    }

    public ProcessPaymentRequestDTO(String uid, List<Long> itemIds) {
        this.uid = uid;
        this.itemIds = itemIds;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}