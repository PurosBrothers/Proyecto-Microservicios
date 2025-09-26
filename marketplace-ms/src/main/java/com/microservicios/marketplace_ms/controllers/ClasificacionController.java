package com.microservicios.marketplace_ms.controllers;

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
import com.microservicios.marketplace_ms.dtos.ClasificacionDTO;
import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.mappers.ClasificacionMapper;
import com.microservicios.marketplace_ms.services.ClasificacionService;

@RestController
@RequestMapping("/clasificaciones")
public class ClasificacionController {

    @Autowired
    private ClasificacionService clasificacionService;

    @PostMapping
    public ResponseEntity<ClasificacionDTO> createItem(@RequestBody ClasificacionDTO item) {
        Clasificacion clasificacion = ClasificacionMapper.dtoToEntity(item);
        ResponseEntity<Clasificacion> response = clasificacionService.createClasificacion(clasificacion);
        if (response.getStatusCode().is2xxSuccessful()) {
            ClasificacionDTO dto = ClasificacionMapper.entityToDto(response.getBody());
            return ResponseEntity.status(response.getStatusCode()).body(dto);
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClasificacionDTO> getItem(@PathVariable Long id) {
        ResponseEntity<Clasificacion> response = clasificacionService.getClasificacion(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            ClasificacionDTO dto = ClasificacionMapper.entityToDto(response.getBody());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClasificacionDTO> updateItem(@PathVariable Long id, @RequestBody ClasificacionDTO item) {
        Clasificacion clasificacion = ClasificacionMapper.dtoToEntity(item);
        ResponseEntity<Clasificacion> response = clasificacionService.updateClasificacion(id, clasificacion);
        if (response.getStatusCode().is2xxSuccessful()) {
            ClasificacionDTO dto = ClasificacionMapper.entityToDto(response.getBody());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        return clasificacionService.deleteClasificacion(id);
    }
}
