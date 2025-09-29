package com.microservicios.gateway_server.validation;

import com.microservicios.gateway_server.enums.TipoUsuario;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador personalizado para TipoUsuario
 */
public class TipoUsuarioValidator implements ConstraintValidator<ValidTipoUsuario, TipoUsuario> {

    @Override
    public void initialize(ValidTipoUsuario constraintAnnotation) {
        // No se necesita inicialización adicional
    }

    @Override
    public boolean isValid(TipoUsuario tipoUsuario, ConstraintValidatorContext context) {
        if (tipoUsuario == null) {
            return false;
        }

        // Verificar que sea uno de los valores permitidos
        return tipoUsuario == TipoUsuario.CLIENTE || tipoUsuario == TipoUsuario.PROVEEDOR;
    }
}