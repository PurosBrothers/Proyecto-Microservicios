package com.microservicios.gateway_server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import com.microservicios.gateway_server.dto.RegistroUsuarioDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserClientService {

    private final WebClient.Builder webClientBuilder;

    @Value("${user-ms.url:http://localhost:8083}")
    private String userMsUrl;

    /**
     * Crea un usuario en el microservicio de usuarios
     */
    public Mono<Void> createUser(RegistroUsuarioDto registroDto, String keycloakUserId) {
        log.info("📡 Enviando solicitud de creación de usuario a user-ms: {}", registroDto.getCorreo());
        log.info("🔍 DEBUG - Datos que se enviarán a user-ms:");
        log.info("   - Keycloak ID: '{}'", keycloakUserId);
        log.info("   - Nombre: '{}'", registroDto.getNombre());
        log.info("   - Apellido: '{}'", registroDto.getApellido());
        log.info("   - Correo: '{}'", registroDto.getCorreo());
        log.info("   - Edad: {}", registroDto.getEdad());
        log.info("   - Descripción: '{}'", registroDto.getDescripcion());
        log.info("   - Teléfono: '{}'", registroDto.getTelefono());
        log.info("   - Dirección: '{}'", registroDto.getDireccion());
        log.info("   - Tipo Usuario: '{}'", registroDto.getTipoUsuario());
        
        // Logging específico para PROVEEDOR
        if (registroDto.getTipoUsuario().name().equals("PROVEEDOR")) {
            log.info("   - Página Web: '{}'", registroDto.getPaginaWeb());
            log.info("   - Redes Sociales: {}", registroDto.getRedesSociales());
            log.info("   - Redes Sociales (tipo): {}", registroDto.getRedesSociales() != null ? registroDto.getRedesSociales().getClass().getSimpleName() : "null");
            log.info("   - Calificación Promedio: {}", registroDto.getCalificacionPromedio());
        }

        // Crear el DTO para enviar a user-ms
        var userRequest = new UserCreationRequest(
            keycloakUserId,
            registroDto.getNombre(),
            registroDto.getApellido(),
            registroDto.getCorreo(),
            registroDto.getEdad(),
            registroDto.getDescripcion(),
            registroDto.getTelefono(),
            registroDto.getDireccion(),
            registroDto.getTipoUsuario().name(),
            registroDto.getPaginaWeb(),
            registroDto.getRedesSociales(),
            registroDto.getCalificacionPromedio()
        );

        return webClientBuilder.build()
            .post()
            .uri(userMsUrl + "/users/internal/create")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(userRequest)
            .retrieve()
            .bodyToMono(Void.class)
            .doOnSuccess(response -> log.info("✅ Usuario creado exitosamente en user-ms: {}", registroDto.getCorreo()))
            .doOnError(error -> log.error("❌ Error creando usuario en user-ms: {}", error.getMessage()));
    }

    /**
     * DTO interno para la creación de usuarios en user-ms
     */
    public static class UserCreationRequest {
        private String keycloakUserId;
        private String nombre;
        private String apellido;
        private String correo;
        private Integer edad;
        private String descripcion;
        private String telefono;
        private String direccion;
        private String tipoUsuario;
        
        // Campos específicos para PROVEEDOR
        private String paginaWeb;
        private java.util.List<String> redesSociales;
        private Float calificacionPromedio;

        public UserCreationRequest() {}

        public UserCreationRequest(String keycloakUserId, String nombre, String apellido, String correo,
                                 Integer edad, String descripcion, String telefono,
                                 String direccion, String tipoUsuario, String paginaWeb,
                                 java.util.List<String> redesSociales, Float calificacionPromedio) {
            this.keycloakUserId = keycloakUserId;
            this.nombre = nombre;
            this.apellido = apellido;
            this.correo = correo;
            this.edad = edad;
            this.descripcion = descripcion;
            this.telefono = telefono;
            this.direccion = direccion;
            this.tipoUsuario = tipoUsuario;
            this.paginaWeb = paginaWeb;
            this.redesSociales = redesSociales;
            this.calificacionPromedio = calificacionPromedio;
        }

        // Getters y setters
        public String getKeycloakUserId() { return keycloakUserId; }
        public void setKeycloakUserId(String keycloakUserId) { this.keycloakUserId = keycloakUserId; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public Integer getEdad() { return edad; }
        public void setEdad(Integer edad) { this.edad = edad; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }

        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }

        public String getTipoUsuario() { return tipoUsuario; }
        public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

        public String getPaginaWeb() { return paginaWeb; }
        public void setPaginaWeb(String paginaWeb) { this.paginaWeb = paginaWeb; }

        public java.util.List<String> getRedesSociales() { return redesSociales; }
        public void setRedesSociales(java.util.List<String> redesSociales) { this.redesSociales = redesSociales; }

        public Float getCalificacionPromedio() { return calificacionPromedio; }
        public void setCalificacionPromedio(Float calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }
    }
}