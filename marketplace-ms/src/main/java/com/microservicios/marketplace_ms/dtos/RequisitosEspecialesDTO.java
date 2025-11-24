package com.microservicios.marketplace_ms.dtos;

public class RequisitosEspecialesDTO {
    
    private Long id;
    private String requisito;

    // Constructors
    public RequisitosEspecialesDTO() {
    }

    public RequisitosEspecialesDTO(Long id, String requisito) {
        this.id = id;
        this.requisito = requisito;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequisito() {
        return requisito;
    }

    public void setRequisito(String requisito) {
        this.requisito = requisito;
    }
}