package com.microservicios.transaction_ms.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransaccionDTO {
    private Long id;
    private String UID;
    private String estado; // "PENDING", "COMPLETED", "FAILED"
    private LocalDate fechaTransaccion;
    private BigDecimal montoTotal;
    private String codigoConfirmacion;
    private String observaciones;
    private List<ItemTransaccionDTO> itemsPagados;
    private List<ItemCarritoDTO> itemsPorPagar;

    public TransaccionDTO() {
    }

    public TransaccionDTO(Long id, String UID, String estado, LocalDate fechaTransaccion, BigDecimal montoTotal,
            String codigoConfirmacion, String observaciones, List<ItemTransaccionDTO> itemsPagados,
            List<ItemCarritoDTO> itemsPorPagar) {
        this.id = id;
        this.UID = UID;
        this.estado = estado;
        this.fechaTransaccion = fechaTransaccion;
        this.montoTotal = montoTotal;
        this.codigoConfirmacion = codigoConfirmacion;
        this.observaciones = observaciones;
        this.itemsPagados = itemsPagados;
        this.itemsPorPagar = itemsPorPagar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String uID) {
        UID = uID;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDate fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getCodigoConfirmacion() {
        return codigoConfirmacion;
    }

    public void setCodigoConfirmacion(String codigoConfirmacion) {
        this.codigoConfirmacion = codigoConfirmacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<ItemTransaccionDTO> getItemsPagados() {
        return itemsPagados;
    }

    public void setItemsPagados(List<ItemTransaccionDTO> itemsPagados) {
        this.itemsPagados = itemsPagados;
    }

    public List<ItemCarritoDTO> getItemsPorPagar() {
        return itemsPorPagar;
    }

    public void setItemsPorPagar(List<ItemCarritoDTO> itemsPorPagar) {
        this.itemsPorPagar = itemsPorPagar;
    }
}