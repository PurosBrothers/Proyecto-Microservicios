package com.microservicios.user_ms.enums;

/**
 * Enumeración para los tipos de usuario en el sistema
 */
public enum TipoUsuario {
    CLIENTE("CLIENTE", "Usuario que consume servicios"),
    PROVEEDOR("PROVEEDOR", "Usuario que ofrece servicios");

    private final String nombre;
    private final String descripcion;

    TipoUsuario(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return nombre;
    }
}