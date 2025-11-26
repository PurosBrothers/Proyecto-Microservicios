package com.microservicios.transaction_ms.dtos;

public class PaymentConfirmationMessageDTO {
    private String uid;
    private Long reservaId;
    private String estadoPago; // Como enum no está compartido, usar String
    private String referencia;

    public PaymentConfirmationMessageDTO() {
    }

    public PaymentConfirmationMessageDTO(String uid, Long reservaId, String estadoPago, String referencia) {
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

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}