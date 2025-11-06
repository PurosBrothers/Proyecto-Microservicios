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

import com.microservicios.marketplace_ms.dtos.TransporteDTO;
import com.microservicios.marketplace_ms.entities.Transporte;
import com.microservicios.marketplace_ms.mappers.TransporteMapper;
import com.microservicios.marketplace_ms.services.ClasificacionService;

@RestController
@RequestMapping("/transporte")
public class TransporteController {

    @Autowired
    private ClasificacionService clasificacionService;

    @Autowired
    private TransporteMapper transporteMapper;

    @PostMapping
    public ResponseEntity<TransporteDTO> createTransporte(@RequestBody TransporteDTO transporteDTO) {
        Transporte transporte = transporteMapper.toEntity(transporteDTO);
        Transporte saved = (Transporte) clasificacionService.createClasificacion(transporte);
        TransporteDTO savedDTO = transporteMapper.toDto(saved);
        return ResponseEntity.ok(savedDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransporteDTO> getTransporte(@PathVariable Long id) {
        Optional<Transporte> transporte = clasificacionService.getTransporteById(id);
        return transporte
                .map(t -> ResponseEntity.ok(transporteMapper.toDto(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TransporteDTO>> getAllTransportes() {
        List<Transporte> transportes = clasificacionService.getAllTransportes();
        List<TransporteDTO> transportesDTO = transportes.stream()
                .map(transporteMapper::toDto)
                .toList();
        return ResponseEntity.ok(transportesDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransporteDTO> updateTransporte(@PathVariable Long id, @RequestBody TransporteDTO transporteDTO) {
        try {
            Transporte transporte = transporteMapper.toEntity(transporteDTO);
            Transporte updated = (Transporte) clasificacionService.updateClasificacion(id, transporte);
            TransporteDTO updatedDTO = transporteMapper.toDto(updated);
            return ResponseEntity.ok(updatedDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransporte(@PathVariable Long id) {
        clasificacionService.deleteClasificacion(id);
        return ResponseEntity.noContent().build();
    }
}