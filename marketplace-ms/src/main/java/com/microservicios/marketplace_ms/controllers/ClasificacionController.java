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

import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.services.ClasificacionService;

@RestController
@RequestMapping("/clasificaciones")
public class ClasificacionController {

    @Autowired
    private ClasificacionService clasificacionService;

    @PostMapping
    public ResponseEntity<Clasificacion> createClasificacion(@RequestBody Clasificacion clasificacion) {
        Clasificacion saved = clasificacionService.createClasificacion(clasificacion);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clasificacion> getClasificacion(@PathVariable Long id) {
        return clasificacionService.getClasificacionById(id)
                .map(clasificacion -> ResponseEntity.ok(clasificacion))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Clasificacion>> getAllClasificaciones() {
        List<Clasificacion> clasificaciones = clasificacionService.getAllClasificaciones();
        return ResponseEntity.ok(clasificaciones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clasificacion> updateClasificacion(@PathVariable Long id,
            @RequestBody Clasificacion clasificacion) {
        try {
            Clasificacion updated = clasificacionService.updateClasificacion(id, clasificacion);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasificacion(@PathVariable Long id) {
        clasificacionService.deleteClasificacion(id);
        return ResponseEntity.noContent().build();
    }
}
