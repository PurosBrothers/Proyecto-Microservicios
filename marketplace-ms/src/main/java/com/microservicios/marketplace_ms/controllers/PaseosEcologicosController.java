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

import com.microservicios.marketplace_ms.entities.PaseosEcologicos;
import com.microservicios.marketplace_ms.repositories.PaseosEcologicosRepository;

@RestController
@RequestMapping("/paseos-ecologicos")
public class PaseosEcologicosController {

    @Autowired
    private PaseosEcologicosRepository paseosEcologicosRepository;

    @PostMapping
    public ResponseEntity<PaseosEcologicos> createPaseosEcologicos(@RequestBody PaseosEcologicos paseosEcologicos) {
        PaseosEcologicos saved = paseosEcologicosRepository.save(paseosEcologicos);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaseosEcologicos> getPaseosEcologicos(@PathVariable Long id) {
        return paseosEcologicosRepository.findById(id)
                .map(paseos -> ResponseEntity.ok(paseos))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PaseosEcologicos>> getAllPaseosEcologicos() {
        List<PaseosEcologicos> paseos = paseosEcologicosRepository.findAll();
        return ResponseEntity.ok(paseos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaseosEcologicos> updatePaseosEcologicos(@PathVariable Long id, @RequestBody PaseosEcologicos paseosEcologicos) {
        if (!paseosEcologicosRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paseosEcologicos.setId(id);
        PaseosEcologicos updated = paseosEcologicosRepository.save(paseosEcologicos);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaseosEcologicos(@PathVariable Long id) {
        if (!paseosEcologicosRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paseosEcologicosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}