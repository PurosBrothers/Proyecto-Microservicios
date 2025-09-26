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

import com.microservicios.marketplace_ms.entities.Alimentacion;
import com.microservicios.marketplace_ms.repositories.AlimentacionRepository;

@RestController
@RequestMapping("/alimentaciones")
public class AlimentacionController {

    @Autowired
    private AlimentacionRepository alimentacionRepository;

    @PostMapping
    public ResponseEntity<Alimentacion> createAlimentacion(@RequestBody Alimentacion alimentacion) {
        Alimentacion saved = alimentacionRepository.save(alimentacion);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alimentacion> getAlimentacion(@PathVariable Long id) {
        return alimentacionRepository.findById(id)
                .map(alimentacion -> ResponseEntity.ok(alimentacion))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Alimentacion>> getAllAlimentaciones() {
        List<Alimentacion> alimentaciones = alimentacionRepository.findAll();
        return ResponseEntity.ok(alimentaciones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alimentacion> updateAlimentacion(@PathVariable Long id, @RequestBody Alimentacion alimentacion) {
        if (!alimentacionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alimentacion.setId(id);
        Alimentacion updated = alimentacionRepository.save(alimentacion);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlimentacion(@PathVariable Long id) {
        if (!alimentacionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alimentacionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}