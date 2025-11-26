package com.microservicios.gateway_server.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validación personalizada para TipoUsuario
 */
@Constraint(validatedBy = TipoUsuarioValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTipoUsuario {
    String message() default "El tipo de usuario debe ser CLIENTE o PROVEEDOR";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}