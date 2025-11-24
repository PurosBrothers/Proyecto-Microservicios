package com.microservicios.marketplace_ms.exceptions;

/**
 * Excepción lanzada cuando se intenta crear/actualizar una clasificación 
 * con un usuario que no es proveedor
 */
public class InvalidProviderException extends RuntimeException {

    public InvalidProviderException(String message) {
        super(message);
    }

    public InvalidProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}