package com.microservicios.marketplace_ms.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Componente para extraer información del JWT del usuario autenticado
 */
@Component
public class JwtSecurityContext {

    /**
     * Obtiene el ID del usuario actual desde el JWT
     * @return ID del usuario o null si no está autenticado
     */
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            Jwt jwt = jwtToken.getToken();
            // El ID del usuario puede estar en 'sub' o 'preferred_username' dependiendo de la configuración de Keycloak
            String userId = jwt.getClaimAsString("sub");
            if (userId == null || userId.isEmpty()) {
                userId = jwt.getClaimAsString("preferred_username");
            }
            return userId;
        }
        
        return null;
    }

    /**
     * Obtiene los roles del usuario actual
     * @return Lista de roles o lista vacía si no está autenticado
     */
    public List<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            return authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                    .collect(Collectors.toList());
        }
        
        return List.of();
    }

    /**
     * Verifica si el usuario actual tiene un rol específico
     * @param role Rol a verificar (sin el prefijo ROLE_)
     * @return true si tiene el rol, false en caso contrario
     */
    public boolean hasRole(String role) {
        return getCurrentUserRoles().contains(role);
    }

    /**
     * Verifica si el usuario actual es un proveedor
     * @return true si es proveedor, false en caso contrario
     */
    public boolean isProveedor() {
        return hasRole("PROVEEDOR");
    }

    /**
     * Verifica si el usuario actual es un cliente
     * @return true si es cliente, false en caso contrario
     */
    public boolean isCliente() {
        return hasRole("CLIENTE");
    }

    /**
     * Obtiene información adicional del JWT
     * @param claimName Nombre del claim a extraer
     * @return Valor del claim o null si no existe
     */
    public String getClaim(String claimName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            Jwt jwt = jwtToken.getToken();
            return jwt.getClaimAsString(claimName);
        }
        
        return null;
    }

    /**
     * Obtiene el email del usuario desde el JWT
     * @return Email del usuario o null si no está disponible
     */
    public String getCurrentUserEmail() {
        return getClaim("email");
    }

    /**
     * Obtiene el nombre del usuario desde el JWT
     * @return Nombre del usuario o null si no está disponible
     */
    public String getCurrentUserName() {
        String name = getClaim("name");
        if (name == null) {
            name = getClaim("preferred_username");
        }
        return name;
    }

    /**
     * Verifica si hay un usuario autenticado
     * @return true si hay un usuario autenticado, false en caso contrario
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !(authentication.getPrincipal().equals("anonymousUser"));
    }
}