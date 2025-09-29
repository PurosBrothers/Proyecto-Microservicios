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

    @Value("${user-ms.url:http://user-ms:8083}")
    private String userMsUrl;

    /**
     * Crea un usuario en el microservicio de usuarios
     */
    public Mono<Void> createUser(RegistroUsuarioDto registroDto, String keycloakUserId) {
        log.info("📡 Enviando solicitud de creación de usuario a user-ms: {}", registroDto.getCorreo());

        // Crear el DTO para enviar a user-ms
        var userRequest = new UserCreationRequest(
            keycloakUserId,
            registroDto.getNombre(),
            registroDto.getCorreo(),
            registroDto.getEdad(),
            registroDto.getDescripcion(),
            registroDto.getTelefono(),
            registroDto.getDireccion(),
            registroDto.getTipoUsuario().name()
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
        private String correo;
        private Integer edad;
        private String descripcion;
        private String telefono;
        private String direccion;
        private String tipoUsuario;

        public UserCreationRequest() {}

        public UserCreationRequest(String keycloakUserId, String nombre, String correo,
                                 Integer edad, String descripcion, String telefono,
                                 String direccion, String tipoUsuario) {
            this.keycloakUserId = keycloakUserId;
            this.nombre = nombre;
            this.correo = correo;
            this.edad = edad;
            this.descripcion = descripcion;
            this.telefono = telefono;
            this.direccion = direccion;
            this.tipoUsuario = tipoUsuario;
        }

        // Getters y setters
        public String getKeycloakUserId() { return keycloakUserId; }
        public void setKeycloakUserId(String keycloakUserId) { this.keycloakUserId = keycloakUserId; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

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
    }
}