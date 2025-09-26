package com.microservicios.marketplace_ms.controllers;

import java.util.List;

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

import com.microservicios.marketplace_ms.entities.Transporte;
import com.microservicios.marketplace_ms.repositories.TransporteRepository;

@RestController
@RequestMapping("/transportes")
public class TransporteController {

    @Autowired
    private TransporteRepository transporteRepository;

    @PostMapping
    public ResponseEntity<Transporte> createTransporte(@RequestBody Transporte transporte) {
        Transporte saved = transporteRepository.save(transporte);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transporte> getTransporte(@PathVariable Long id) {
        return transporteRepository.findById(id)
                .map(transporte -> ResponseEntity.ok(transporte))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Transporte>> getAllTransportes() {
        List<Transporte> transportes = transporteRepository.findAll();
        return ResponseEntity.ok(transportes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transporte> updateTransporte(@PathVariable Long id, @RequestBody Transporte transporte) {
        if (!transporteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        transporte.setId(id);
        Transporte updated = transporteRepository.save(transporte);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransporte(@PathVariable Long id) {
        if (!transporteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        transporteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}