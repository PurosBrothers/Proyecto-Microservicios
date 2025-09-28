package com.microservicios.user_ms.validation;

import com.microservicios.user_ms.enums.TipoUsuario;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador personalizado para TipoUsuario
 */
public class TipoUsuarioValidator implements ConstraintValidator<ValidTipoUsuario, TipoUsuario> {

    @Override
    public void initialize(ValidTipoUsuario constraintAnnotation) {
        // Inicialización si es necesaria
    }

    @Override
    public boolean isValid(TipoUsuario tipoUsuario, ConstraintValidatorContext context) {
        // El tipo de usuario no puede ser null
        if (tipoUsuario == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El tipo de usuario es obligatorio")
                   .addConstraintViolation();
            return false;
        }

        // Verificar que sea uno de los tipos válidos
        return tipoUsuario == TipoUsuario.CLIENTE || tipoUsuario == TipoUsuario.PROVEEDOR;
    }
}