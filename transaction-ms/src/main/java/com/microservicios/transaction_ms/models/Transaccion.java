package com.microservicios.transaction_ms.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String UID;
    private String estado; // "PENDING", "COMPLETED", "FAILED"
    private LocalDate fechaTransaccion;
    private BigDecimal montoTotal;
    private String codigoConfirmacion;
    private String observaciones;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemTransaccion> itemsPagados;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemTransaccion> itemsPorPagar;

    public Transaccion() {
    }

    public Transaccion(Long id, String UID, String estado, LocalDate fechaTransaccion, BigDecimal montoTotal,
            String codigoConfirmacion, String observaciones, List<ItemTransaccion> itemsPagados,
            List<ItemTransaccion> itemsPorPagar) {
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

    public List<ItemTransaccion> getItemsPagados() {
        return itemsPagados;
    }

    public void setItemsPagados(List<ItemTransaccion> itemsPagados) {
        this.itemsPagados = itemsPagados;
    }

    public List<ItemTransaccion> getItemsPorPagar() {
        return itemsPorPagar;
    }

    public void setItemsPorPagar(List<ItemTransaccion> itemsPorPagar) {
        this.itemsPorPagar = itemsPorPagar;
    }

}
