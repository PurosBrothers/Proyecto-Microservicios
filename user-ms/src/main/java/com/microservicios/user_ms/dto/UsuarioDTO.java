package com.microservicios.user_ms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class UsuarioDTO {

    private String id;

    @NotBlank
    private String nombre;

    @NotNull
    private Integer edad;

    private String fotoUrl;

    private String descripcion;

    @NotBlank
    @Email
    private String correo;

    private ZonedDateTime fechaRegistro;

    private String direccion;

    private String telefono;

    private String paginaWeb;

    private List<String> redesSociales;

    private Float calificacionPromedio;
}