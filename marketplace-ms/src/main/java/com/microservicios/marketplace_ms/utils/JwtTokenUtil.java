package com.microservicios.marketplace_ms.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Utilidad para extraer información del token JWT de Keycloak
 */
@Component
public class JwtTokenUtil {
    
    /**
     * Extrae el UID (subject) del token JWT actual
     * @return UID del usuario autenticado o null si no hay token
     */
    public String extractUidFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            return jwt.getSubject(); // El 'sub' claim contiene el UID de Keycloak
        }
        
        return null;
    }
    
    /**
     * Extrae el email del token JWT actual
     * @return Email del usuario autenticado o null si no hay token
     */
    public String extractEmailFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            return jwt.getClaim("email");
        }
        
        return null;
    }
    
    /**
     * Verifica si el usuario actual es el propietario del recurso
     * @param resourceOwnerUid UID del propietario del recurso
     * @return true si el usuario actual es el propietario
     */
    public boolean isResourceOwner(String resourceOwnerUid) {
        String currentUserUid = extractUidFromToken();
        return currentUserUid != null && currentUserUid.equals(resourceOwnerUid);
    }
}