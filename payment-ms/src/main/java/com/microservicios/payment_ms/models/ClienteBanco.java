package com.microservicios.payment_ms.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cliente_banco")
public class ClienteBanco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uid;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String cuentaBancariaEncrypted;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String claveBancariaEncrypted;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    // Constructores
    public ClienteBanco() {
    }

    public ClienteBanco(String uid, String cuentaBancariaEncrypted, String claveBancariaEncrypted) {
        this.uid = uid;
        this.cuentaBancariaEncrypted = cuentaBancariaEncrypted;
        this.claveBancariaEncrypted = claveBancariaEncrypted;
        this.saldo = BigDecimal.ZERO;
    }

    public ClienteBanco(String uid, String cuentaBancariaEncrypted, String claveBancariaEncrypted, BigDecimal saldo) {
        this.uid = uid;
        this.cuentaBancariaEncrypted = cuentaBancariaEncrypted;
        this.claveBancariaEncrypted = claveBancariaEncrypted;
        this.saldo = saldo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCuentaBancariaEncrypted() {
        return cuentaBancariaEncrypted;
    }

    public void setCuentaBancariaEncrypted(String cuentaBancariaEncrypted) {
        this.cuentaBancariaEncrypted = cuentaBancariaEncrypted;
    }

    public String getClaveBancariaEncrypted() {
        return claveBancariaEncrypted;
    }

    public void setClaveBancariaEncrypted(String claveBancariaEncrypted) {
        this.claveBancariaEncrypted = claveBancariaEncrypted;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}