package com.microservicios.user_ms.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("PROVEEDOR")
@Getter
@Setter
public class Proveedor extends Usuario {

    private String telefono;

    private String paginaWeb;

    @ElementCollection
    private List<String> redesSociales;

    private Float calificacionPromedio;
}