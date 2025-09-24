package com.microservicios.user_ms.mapper;

import com.microservicios.user_ms.dto.UsuarioDTO;
import com.microservicios.user_ms.entity.Cliente;
import com.microservicios.user_ms.entity.Proveedor;
import com.microservicios.user_ms.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setEdad(usuario.getEdad());
        usuarioDTO.setFotoUrl(usuario.getFotoUrl());
        usuarioDTO.setDescripcion(usuario.getDescripcion());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setFechaRegistro(usuario.getFechaRegistro());

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            usuarioDTO.setDireccion(cliente.getDireccion());
            usuarioDTO.setTelefono(cliente.getTelefono());
        } else if (usuario instanceof Proveedor) {
            Proveedor proveedor = (Proveedor) usuario;
            usuarioDTO.setTelefono(proveedor.getTelefono());
            usuarioDTO.setPaginaWeb(proveedor.getPaginaWeb());
            usuarioDTO.setRedesSociales(proveedor.getRedesSociales());
            usuarioDTO.setCalificacionPromedio(proveedor.getCalificacionPromedio());
        }

        return usuarioDTO;
    }

    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) {
            return null;
        }

        if (usuarioDTO.getDireccion() != null || usuarioDTO.getTelefono() != null) {
            Cliente cliente = new Cliente();
            cliente.setNombre(usuarioDTO.getNombre());
            cliente.setEdad(usuarioDTO.getEdad());
            cliente.setFotoUrl(usuarioDTO.getFotoUrl());
            cliente.setDescripcion(usuarioDTO.getDescripcion());
            cliente.setCorreo(usuarioDTO.getCorreo());
            cliente.setDireccion(usuarioDTO.getDireccion());
            cliente.setTelefono(usuarioDTO.getTelefono());
            return cliente;
        } else if (usuarioDTO.getPaginaWeb() != null || usuarioDTO.getRedesSociales() != null || usuarioDTO.getCalificacionPromedio() != null) {
            Proveedor proveedor = new Proveedor();
            proveedor.setNombre(usuarioDTO.getNombre());
            proveedor.setEdad(usuarioDTO.getEdad());
            proveedor.setFotoUrl(usuarioDTO.getFotoUrl());
            proveedor.setDescripcion(usuarioDTO.getDescripcion());
            proveedor.setCorreo(usuarioDTO.getCorreo());
            proveedor.setTelefono(usuarioDTO.getTelefono());
            proveedor.setPaginaWeb(usuarioDTO.getPaginaWeb());
            proveedor.setRedesSociales(usuarioDTO.getRedesSociales());
            proveedor.setCalificacionPromedio(usuarioDTO.getCalificacionPromedio());
            return proveedor;
        } else {
            // Default to Cliente if no specific fields
            Cliente cliente = new Cliente();
            cliente.setNombre(usuarioDTO.getNombre());
            cliente.setEdad(usuarioDTO.getEdad());
            cliente.setFotoUrl(usuarioDTO.getFotoUrl());
            cliente.setDescripcion(usuarioDTO.getDescripcion());
            cliente.setCorreo(usuarioDTO.getCorreo());
            return cliente;
        }
    }
}