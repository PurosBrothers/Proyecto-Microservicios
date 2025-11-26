package com.microservicios.user_ms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("PROVEEDOR")
@Getter
@Setter
public class Proveedor extends Usuario {

    private String paginaWeb;

    @ElementCollection
    @CollectionTable(name = "proveedor_redes_sociales", joinColumns = @JoinColumn(name = "proveedor_id"))
    @Column(name = "red_social")
    private List<String> redesSociales;

    private Float calificacionPromedio;
}