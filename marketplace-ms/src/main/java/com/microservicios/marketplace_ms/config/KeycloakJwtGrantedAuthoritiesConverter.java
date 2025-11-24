package com.microservicios.marketplace_ms.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Converter personalizado para extraer roles de Keycloak JWT tokens
 */
public class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Extraer roles de realm_access
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null && realmAccess.get("roles") instanceof List) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            }
        }
        
        // Extraer roles de resource_access (para clientes específicos)
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess != null) {
            for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    Map<String, Object> clientRoles = (Map<String, Object>) entry.getValue();
                    if (clientRoles.containsKey("roles") && clientRoles.get("roles") instanceof List) {
                        List<String> roles = (List<String>) clientRoles.get("roles");
                        for (String role : roles) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                        }
                    }
                }
            }
        }
        
        return authorities;
    }
}