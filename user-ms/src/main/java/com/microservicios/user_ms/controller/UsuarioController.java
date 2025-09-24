package com.microservicios.user_ms.controller;

import com.microservicios.user_ms.dto.UsuarioDTO;
import com.microservicios.user_ms.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO created = usuarioService.createUsuario(usuarioDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable String id) {
        UsuarioDTO usuario = usuarioService.getUsuario(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable String id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO updated = usuarioService.updateUsuario(id, usuarioDTO);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UsuarioDTO> deleteUsuario(@PathVariable String id) {
        UsuarioDTO deleted = usuarioService.deleteUsuario(id);
        if (deleted != null) {
            return ResponseEntity.ok(deleted);
        }
        return ResponseEntity.notFound().build();
    }
}