package com.microservicios.user_ms.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Anotación para validar que el tipo de usuario sea válido
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TipoUsuarioValidator.class)
@Documented
public @interface ValidTipoUsuario {
    String message() default "El tipo de usuario debe ser CLIENTE o PROVEEDOR y es obligatorio";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}