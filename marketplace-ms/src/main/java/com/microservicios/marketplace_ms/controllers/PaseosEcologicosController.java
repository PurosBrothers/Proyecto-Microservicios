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

import com.microservicios.marketplace_ms.dtos.PaseosEcologicosDTO;
import com.microservicios.marketplace_ms.entities.PaseosEcologicos;
import com.microservicios.marketplace_ms.mappers.PaseosEcologicosMapper;
import com.microservicios.marketplace_ms.services.ClasificacionService;

@RestController
@RequestMapping("/paseos-ecologico")
public class PaseosEcologicosController {

    @Autowired
    private ClasificacionService clasificacionService;

    @Autowired
    private PaseosEcologicosMapper paseosEcologicosMapper;

    @PostMapping
    public ResponseEntity<PaseosEcologicosDTO> createPaseosEcologicos(@RequestBody PaseosEcologicosDTO paseosEcologicosDTO) {
        PaseosEcologicos paseosEcologicos = paseosEcologicosMapper.toEntity(paseosEcologicosDTO);
        PaseosEcologicos saved = (PaseosEcologicos) clasificacionService.createClasificacion(paseosEcologicos);
        PaseosEcologicosDTO savedDTO = paseosEcologicosMapper.toDto(saved);
        return ResponseEntity.ok(savedDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaseosEcologicosDTO> getPaseosEcologicos(@PathVariable Long id) {
        Optional<PaseosEcologicos> paseosEcologicos = clasificacionService.getPaseosEcologicosById(id);
        return paseosEcologicos
                .map(p -> ResponseEntity.ok(paseosEcologicosMapper.toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PaseosEcologicosDTO>> getAllPaseosEcologicos() {
        List<PaseosEcologicos> paseosEcologicos = clasificacionService.getAllPaseosEcologicos();
        List<PaseosEcologicosDTO> paseosEcologicosDTO = paseosEcologicos.stream()
                .map(paseosEcologicosMapper::toDto)
                .toList();
        return ResponseEntity.ok(paseosEcologicosDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaseosEcologicosDTO> updatePaseosEcologicos(@PathVariable Long id, @RequestBody PaseosEcologicosDTO paseosEcologicosDTO) {
        try {
            PaseosEcologicos paseosEcologicos = paseosEcologicosMapper.toEntity(paseosEcologicosDTO);
            PaseosEcologicos updated = (PaseosEcologicos) clasificacionService.updateClasificacion(id, paseosEcologicos);
            PaseosEcologicosDTO updatedDTO = paseosEcologicosMapper.toDto(updated);
            return ResponseEntity.ok(updatedDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaseosEcologicos(@PathVariable Long id) {
        clasificacionService.deleteClasificacion(id);
        return ResponseEntity.noContent().build();
    }
}