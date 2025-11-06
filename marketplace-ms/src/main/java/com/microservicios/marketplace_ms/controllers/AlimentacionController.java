package com.microservicios.marketplace_ms.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicios.marketplace_ms.dtos.AlimentacionDTO;
import com.microservicios.marketplace_ms.entities.Alimentacion;
import com.microservicios.marketplace_ms.mappers.AlimentacionMapper;
import com.microservicios.marketplace_ms.services.ClasificacionService;

@RestController
@RequestMapping("/alimentacion")
public class AlimentacionController {

    @Autowired
    private ClasificacionService clasificacionService;

    @Autowired
    private AlimentacionMapper alimentacionMapper;

    @PostMapping
    public ResponseEntity<AlimentacionDTO> createAlimentacion(@RequestBody AlimentacionDTO alimentacionDTO) {
        Alimentacion alimentacion = alimentacionMapper.toEntity(alimentacionDTO);
        Alimentacion saved = (Alimentacion) clasificacionService.createClasificacion(alimentacion);
        AlimentacionDTO savedDTO = alimentacionMapper.toDto(saved);
        return ResponseEntity.ok(savedDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlimentacionDTO> getAlimentacion(@PathVariable Long id) {
        Optional<Alimentacion> alimentacion = clasificacionService.getAlimentacionById(id);
        return alimentacion
                .map(a -> ResponseEntity.ok(alimentacionMapper.toDto(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AlimentacionDTO>> getAllAlimentaciones() {
        List<Alimentacion> alimentaciones = clasificacionService.getAllAlimentaciones();
        List<AlimentacionDTO> alimentacionesDTO = alimentaciones.stream()
                .map(alimentacionMapper::toDto)
                .toList();
        return ResponseEntity.ok(alimentacionesDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlimentacionDTO> updateAlimentacion(@PathVariable Long id, @RequestBody AlimentacionDTO alimentacionDTO) {
        try {
            Alimentacion alimentacion = alimentacionMapper.toEntity(alimentacionDTO);
            Alimentacion updated = (Alimentacion) clasificacionService.updateClasificacion(id, alimentacion);
            AlimentacionDTO updatedDTO = alimentacionMapper.toDto(updated);
            return ResponseEntity.ok(updatedDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlimentacion(@PathVariable Long id) {
        clasificacionService.deleteClasificacion(id);
        return ResponseEntity.noContent().build();
    }
}