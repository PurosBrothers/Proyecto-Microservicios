package com.microservicios.transaction_ms.dtos;

public class UidRequestDTO {
    private String uid;

    public UidRequestDTO() {
    }

    public UidRequestDTO(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}