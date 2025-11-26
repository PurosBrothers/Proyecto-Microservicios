package com.microservicios.payment_ms.dtos;

import java.math.BigDecimal;
import java.util.List;

public class ProcessPaymentMessageDTO {
    private String uid;
    private Long reservaId;
    private BigDecimal montoTotal;
    private List<ItemPaymentDTO> items;

    public ProcessPaymentMessageDTO() {
    }

    public ProcessPaymentMessageDTO(String uid, Long reservaId, BigDecimal montoTotal, List<ItemPaymentDTO> items) {
        this.uid = uid;
        this.reservaId = reservaId;
        this.montoTotal = montoTotal;
        this.items = items;
    }

    // Getters and Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public List<ItemPaymentDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemPaymentDTO> items) {
        this.items = items;
    }
}