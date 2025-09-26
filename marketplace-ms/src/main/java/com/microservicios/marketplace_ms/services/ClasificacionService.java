package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.repositories.ClasificacionRepository;

@Service
public class ClasificacionService {

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    public Clasificacion createClasificacion(Clasificacion clasificacion) {
        return clasificacionRepository.save(clasificacion);
    }

    public Optional<Clasificacion> getClasificacionById(Long id) {
        return clasificacionRepository.findById(id);
    }

    public List<Clasificacion> getAllClasificaciones() {
        return clasificacionRepository.findAll();
    }

    public Clasificacion updateClasificacion(Long id, Clasificacion clasificacion) {
        if (!clasificacionRepository.existsById(id)) {
            throw new RuntimeException("Clasificacion not found");
        }
        clasificacion.setId(id);
        return clasificacionRepository.save(clasificacion);
    }

    public void deleteClasificacion(Long id) {
        clasificacionRepository.deleteById(id);
    }
}
