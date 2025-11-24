package com.microservicios.marketplace_ms.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicios.marketplace_ms.dtos.AlojamientoDTO;
import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.mappers.AlojamientoMapper;
import com.microservicios.marketplace_ms.services.ClasificacionService;

@RestController
@RequestMapping("/alojamiento")
public class AlojamientoController {

    @Autowired
    private ClasificacionService clasificacionService;

    @Autowired
    private AlojamientoMapper alojamientoMapper;

    @PostMapping
    @PreAuthorize("hasRole('PROVEEDOR')")
    public ResponseEntity<AlojamientoDTO> createAlojamiento(@RequestBody AlojamientoDTO alojamientoDTO) {
        // El usuarioId se establece automáticamente desde el JWT
        Alojamiento alojamiento = alojamientoMapper.toEntity(alojamientoDTO);
        Alojamiento saved = (Alojamiento) clasificacionService.createClasificacion(alojamiento);
        AlojamientoDTO savedDTO = alojamientoMapper.toDto(saved);
        return ResponseEntity.ok(savedDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlojamientoDTO> getAlojamiento(@PathVariable Long id) {
        Optional<Alojamiento> alojamiento = clasificacionService.getAlojamientoById(id);
        return alojamiento
                .map(a -> ResponseEntity.ok(alojamientoMapper.toDto(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AlojamientoDTO>> getAllAlojamientos() {
        List<Alojamiento> alojamientos = clasificacionService.getAllAlojamientos();
        List<AlojamientoDTO> alojamientosDTO = alojamientos.stream()
                .map(alojamientoMapper::toDto)
                .toList();
        return ResponseEntity.ok(alojamientosDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROVEEDOR')")
    public ResponseEntity<AlojamientoDTO> updateAlojamiento(@PathVariable Long id, @RequestBody AlojamientoDTO alojamientoDTO) {
        try {
            // El usuario debe ser el propietario (validado en el servicio)
            Alojamiento alojamiento = alojamientoMapper.toEntity(alojamientoDTO);
            Alojamiento updated = (Alojamiento) clasificacionService.updateClasificacion(id, alojamiento);
            AlojamientoDTO updatedDTO = alojamientoMapper.toDto(updated);
            return ResponseEntity.ok(updatedDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROVEEDOR')")
    public ResponseEntity<Void> deleteAlojamiento(@PathVariable Long id) {
        try {
            // El usuario debe ser el propietario (validado en el servicio)
            clasificacionService.deleteClasificacion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AlojamientoDTO>> getAlojamientosByUsuario(@PathVariable String usuarioId) {
        List<Alojamiento> alojamientos = clasificacionService.getAlojamientosByUsuario(usuarioId);
        List<AlojamientoDTO> alojamientosDTO = alojamientos.stream()
                .map(alojamientoMapper::toDto)
                .toList();
        return ResponseEntity.ok(alojamientosDTO);
    }

    @GetMapping("/mis-alojamientos")
    @PreAuthorize("hasRole('PROVEEDOR')")
    public ResponseEntity<List<AlojamientoDTO>> getMisAlojamientos() {
        List<Alojamiento> alojamientos = clasificacionService.getMyAlojamientos();
        List<AlojamientoDTO> alojamientosDTO = alojamientos.stream()
                .map(alojamientoMapper::toDto)
                .toList();
        return ResponseEntity.ok(alojamientosDTO);
    }
}