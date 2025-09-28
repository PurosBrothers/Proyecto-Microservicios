package com.microservicios.user_ms.dto;

import com.microservicios.user_ms.enums.TipoUsuario;
import com.microservicios.user_ms.validation.ValidTipoUsuario;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO para el registro de usuarios con asignación de roles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDto {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 50, message = "La contraseña debe tener entre 6 y 50 caracteres")
    private String password;

    @NotNull(message = "El tipo de usuario es obligatorio. Debe ser CLIENTE o PROVEEDOR")
    @ValidTipoUsuario
    private TipoUsuario tipoUsuario;

    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 120, message = "La edad máxima es 120 años")
    private Integer edad;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "El formato del teléfono no es válido")
    private String telefono;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String direccion;
}