package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.repositories.ClasificacionRepository;

@Service
public class ClasificacionService {

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    public ResponseEntity<Clasificacion> createClasificacion(Clasificacion clasificacion) {
        Clasificacion saved = clasificacionRepository.save(clasificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public ResponseEntity<Clasificacion> getClasificacion(Long id) {
        Optional<Clasificacion> clasificacion = clasificacionRepository.findById(id);
        return clasificacion.map(c -> ResponseEntity.ok(c)).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Clasificacion> updateClasificacion(Long id, Clasificacion clasificacion) {
        if (!clasificacionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clasificacion.setId(id);
        Clasificacion updated = clasificacionRepository.save(clasificacion);
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<Void> deleteClasificacion(Long id) {
        if (!clasificacionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clasificacionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<Clasificacion>> getAllClasificaciones() {
        List<Clasificacion> clasificaciones = clasificacionRepository.findAll();
        return ResponseEntity.ok(clasificaciones);
    }
}
