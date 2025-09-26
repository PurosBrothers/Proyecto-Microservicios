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

import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.repositories.AlojamientoRepository;

@RestController
@RequestMapping("/alojamientos")
public class AlojamientoController {

    @Autowired
    private AlojamientoRepository alojamientoRepository;

    @PostMapping
    public ResponseEntity<Alojamiento> createAlojamiento(@RequestBody Alojamiento alojamiento) {
        Alojamiento saved = alojamientoRepository.save(alojamiento);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alojamiento> getAlojamiento(@PathVariable Long id) {
        return alojamientoRepository.findById(id)
                .map(alojamiento -> ResponseEntity.ok(alojamiento))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Alojamiento>> getAllAlojamientos() {
        List<Alojamiento> alojamientos = alojamientoRepository.findAll();
        return ResponseEntity.ok(alojamientos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alojamiento> updateAlojamiento(@PathVariable Long id, @RequestBody Alojamiento alojamiento) {
        if (!alojamientoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alojamiento.setId(id);
        Alojamiento updated = alojamientoRepository.save(alojamiento);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlojamiento(@PathVariable Long id) {
        if (!alojamientoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alojamientoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}