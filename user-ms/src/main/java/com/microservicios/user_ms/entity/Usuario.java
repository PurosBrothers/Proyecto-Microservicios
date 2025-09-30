package com.microservicios.user_ms.entity;

import com.microservicios.user_ms.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario")
@Getter
@Setter
public abstract class Usuario {

    @Id
    private String id;

    private String nombre;
    
    private String apellido;

    private Integer edad;

    private String fotoUrl;

    @Lob
    @Column(name = "foto_data", columnDefinition = "BLOB")
    private byte[] fotoData;

    @Column(name = "foto_tipo")
    private String fotoTipo; // image/jpeg, image/png, etc.

    @Column(name = "foto_nombre")
    private String fotoNombre; // nombre original del archivo

    private String descripcion;

    @Column(unique = true)
    private String correo;

    @CreationTimestamp
    private ZonedDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario_enum", nullable = false)
    private TipoUsuario tipoUsuario;

    // Campos adicionales para direcciones (útil para proveedores)
    private String telefono;
    private String direccion;

    /**
     * Validación antes de persistir en base de datos
     */
    @PrePersist
    @PreUpdate
    protected void validateBeforeSave() {
        if (this.tipoUsuario == null) {
            throw new IllegalStateException("El tipo de usuario es obligatorio y no puede ser nulo");
        }
        if (this.correo == null || this.correo.trim().isEmpty()) {
            throw new IllegalStateException("El correo electrónico es obligatorio");
        }
        if (this.nombre == null || this.nombre.trim().isEmpty()) {
            throw new IllegalStateException("El nombre es obligatorio");
        }
    }
}