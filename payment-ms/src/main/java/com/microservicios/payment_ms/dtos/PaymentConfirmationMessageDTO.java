package com.microservicios.payment_ms.dtos;

import com.microservicios.payment_ms.models.EstadoPago;

public class PaymentConfirmationMessageDTO {
    private String uid;
    private Long reservaId;
    private EstadoPago estadoPago;
    private String referencia;

    public PaymentConfirmationMessageDTO() {
    }

    public PaymentConfirmationMessageDTO(String uid, Long reservaId, EstadoPago estadoPago, String referencia) {
        this.uid = uid;
        this.reservaId = reservaId;
        this.estadoPago = estadoPago;
        this.referencia = referencia;
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

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}