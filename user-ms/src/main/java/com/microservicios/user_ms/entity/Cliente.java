package com.microservicios.user_ms.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("CLIENTE")
@Getter
@Setter
public class Cliente extends Usuario {

    // Los campos telefono y direccion ya están definidos en la clase padre Usuario
    // No necesitamos redefinirlos aquí
}