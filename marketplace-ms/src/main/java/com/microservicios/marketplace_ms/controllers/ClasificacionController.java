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

import com.microservicios.marketplace_ms.dtos.ClasificacionDTO;
import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.mappers.ClasificacionMapper;
import com.microservicios.marketplace_ms.services.ClasificacionService;

@RestController
@RequestMapping("/clasificaciones")
public class ClasificacionController {

    @Autowired
    private ClasificacionService clasificacionService;

    @Autowired
    private ClasificacionMapper clasificacionMapper;

    @PostMapping
    public ResponseEntity<Clasificacion> createClasificacion(@RequestBody Clasificacion clasificacion) {
        Clasificacion saved = clasificacionService.createClasificacion(clasificacion);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClasificacionDTO> getClasificacion(@PathVariable Long id) {
        return clasificacionService.getClasificacionById(id)
                .map(clasificacion -> ResponseEntity.ok(clasificacionMapper.toDTO(clasificacion)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClasificacionDTO>> getAllClasificaciones() {
        List<Clasificacion> clasificaciones = clasificacionService.getAllClasificaciones();
        List<ClasificacionDTO> dtoList = clasificacionMapper.toDTOList(clasificaciones);
        return ResponseEntity.ok(dtoList);
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
    
    // Endpoints específicos para cada tipo de clasificación con DTOs
    @GetMapping("/alojamientos")
    public ResponseEntity<List<com.microservicios.marketplace_ms.dtos.AlojamientoDTO>> getAllAlojamientos() {
        return ResponseEntity.ok(clasificacionService.getAllAlojamientos().stream()
                .map(alojamiento -> clasificacionMapper.toAlojamientoDTO(alojamiento))
                .collect(java.util.stream.Collectors.toList()));
    }
    
    @GetMapping("/alojamientos/{id}")
    public ResponseEntity<com.microservicios.marketplace_ms.dtos.AlojamientoDTO> getAlojamiento(@PathVariable Long id) {
        return clasificacionService.getAlojamientoById(id)
                .map(alojamiento -> ResponseEntity.ok(clasificacionMapper.toAlojamientoDTO(alojamiento)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/alimentaciones")
    public ResponseEntity<List<com.microservicios.marketplace_ms.dtos.AlimentacionDTO>> getAllAlimentaciones() {
        return ResponseEntity.ok(clasificacionService.getAllAlimentaciones().stream()
                .map(alimentacion -> clasificacionMapper.toAlimentacionDTO(alimentacion))
                .collect(java.util.stream.Collectors.toList()));
    }
    
    @GetMapping("/alimentaciones/{id}")
    public ResponseEntity<com.microservicios.marketplace_ms.dtos.AlimentacionDTO> getAlimentacion(@PathVariable Long id) {
        return clasificacionService.getAlimentacionById(id)
                .map(alimentacion -> ResponseEntity.ok(clasificacionMapper.toAlimentacionDTO(alimentacion)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/transportes")
    public ResponseEntity<List<com.microservicios.marketplace_ms.dtos.TransporteDTO>> getAllTransportes() {
        return ResponseEntity.ok(clasificacionService.getAllTransportes().stream()
                .map(transporte -> clasificacionMapper.toTransporteDTO(transporte))
                .collect(java.util.stream.Collectors.toList()));
    }
    
    @GetMapping("/transportes/{id}")
    public ResponseEntity<com.microservicios.marketplace_ms.dtos.TransporteDTO> getTransporte(@PathVariable Long id) {
        return clasificacionService.getTransporteById(id)
                .map(transporte -> ResponseEntity.ok(clasificacionMapper.toTransporteDTO(transporte)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/paseos-ecologicos")
    public ResponseEntity<List<com.microservicios.marketplace_ms.dtos.PaseosEcologicosDTO>> getAllPaseosEcologicos() {
        return ResponseEntity.ok(clasificacionService.getAllPaseosEcologicos().stream()
                .map(paseos -> clasificacionMapper.toPaseosEcologicosDTO(paseos))
                .collect(java.util.stream.Collectors.toList()));
    }
    
    @GetMapping("/paseos-ecologicos/{id}")
    public ResponseEntity<com.microservicios.marketplace_ms.dtos.PaseosEcologicosDTO> getPaseosEcologicos(@PathVariable Long id) {
        return clasificacionService.getPaseosEcologicosById(id)
                .map(paseos -> ResponseEntity.ok(clasificacionMapper.toPaseosEcologicosDTO(paseos)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Métodos para obtener clasificaciones del usuario actual
    @GetMapping("/mis-clasificaciones")
    public ResponseEntity<List<ClasificacionDTO>> getMyClasificaciones() {
        List<Clasificacion> clasificaciones = clasificacionService.getMyClasificaciones();
        return ResponseEntity.ok(clasificacionMapper.toDTOList(clasificaciones));
    }
    
    @GetMapping("/mis-alojamientos")
    public ResponseEntity<List<com.microservicios.marketplace_ms.dtos.AlojamientoDTO>> getMyAlojamientos() {
        return ResponseEntity.ok(clasificacionService.getMyAlojamientos().stream()
                .map(alojamiento -> clasificacionMapper.toAlojamientoDTO(alojamiento))
                .collect(java.util.stream.Collectors.toList()));
    }
    
    @GetMapping("/mis-alimentaciones")
    public ResponseEntity<List<com.microservicios.marketplace_ms.dtos.AlimentacionDTO>> getMyAlimentaciones() {
        return ResponseEntity.ok(clasificacionService.getMyAlimentaciones().stream()
                .map(alimentacion -> clasificacionMapper.toAlimentacionDTO(alimentacion))
                .collect(java.util.stream.Collectors.toList()));
    }
    
    @GetMapping("/mis-transportes")
    public ResponseEntity<List<com.microservicios.marketplace_ms.dtos.TransporteDTO>> getMyTransportes() {
        return ResponseEntity.ok(clasificacionService.getMyTransportes().stream()
                .map(transporte -> clasificacionMapper.toTransporteDTO(transporte))
                .collect(java.util.stream.Collectors.toList()));
    }
    
    @GetMapping("/mis-paseos-ecologicos")
    public ResponseEntity<List<com.microservicios.marketplace_ms.dtos.PaseosEcologicosDTO>> getMyPaseosEcologicos() {
        return ResponseEntity.ok(clasificacionService.getMyPaseosEcologicos().stream()
                .map(paseos -> clasificacionMapper.toPaseosEcologicosDTO(paseos))
                .collect(java.util.stream.Collectors.toList()));
    }
}
