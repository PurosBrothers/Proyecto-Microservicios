package com.microservicios.user_ms.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para validaciones
 */
@RestControllerAdvice
@Slf4j
public class ValidationExceptionHandler {

    /**
     * Maneja errores de validación de Bean Validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            
            log.warn("Error de validación en campo '{}': {}", fieldName, errorMessage);
        });
        
        response.put("error", "Errores de validación en los datos proporcionados");
        response.put("validationErrors", errors);
        response.put("status", HttpStatus.BAD_REQUEST.value());
        
        // Verificar específicamente si hay error en tipoUsuario
        if (errors.containsKey("tipoUsuario")) {
            response.put("message", "El tipo de usuario es obligatorio. Debe especificar CLIENTE o PROVEEDOR");
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja excepciones de argumentos ilegales
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        
        log.error("Error de argumento ilegal: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja excepciones generales del sistema
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("error", "Error interno del servidor");
        response.put("message", "Ha ocurrido un error inesperado. Por favor, contacte al administrador.");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        log.error("Error interno del servidor: {}", ex.getMessage(), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}