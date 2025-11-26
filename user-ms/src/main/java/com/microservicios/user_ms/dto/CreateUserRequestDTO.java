package com.microservicios.user_ms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateUserRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotNull(message = "La edad es obligatoria")
    private Integer edad;

    private String fotoUrl;

    private String descripcion;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener formato válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // Campos específicos para Cliente
    private String direccion;
    private String telefono;

    // Campos específicos para Proveedor
    private String paginaWeb;
    private List<String> redesSociales;
    private Float calificacionPromedio;

    @NotBlank(message = "El tipo de usuario es obligatorio (CLIENTE o PROVEEDOR)")
    private String tipoUsuario; // "CLIENTE" o "PROVEEDOR"
}