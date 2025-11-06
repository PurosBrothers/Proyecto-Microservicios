package com.microservicios.marketplace_ms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicios.marketplace_ms.dtos.PreguntaFrecuenteDTO;
import com.microservicios.marketplace_ms.services.PreguntaFrecuenteService;

@RestController
@RequestMapping("/items/{itemId}/preguntas")
public class PreguntaFrecuenteController {

    @Autowired
    private PreguntaFrecuenteService preguntaFrecuenteService;

    @PostMapping
    @PreAuthorize("hasRole('PROVEEDOR')")
    public ResponseEntity<PreguntaFrecuenteDTO> addPregunta(@PathVariable Long itemId, @RequestBody PreguntaFrecuenteDTO preguntaDTO) {
        return preguntaFrecuenteService.addPreguntaToItem(itemId, preguntaDTO);
    }

    @DeleteMapping("/{preguntaId}")
    @PreAuthorize("hasRole('PROVEEDOR')")
    public ResponseEntity<Void> removePregunta(@PathVariable Long itemId, @PathVariable Long preguntaId) {
        return preguntaFrecuenteService.removePreguntaFromItem(itemId, preguntaId);
    }

    @GetMapping
    public ResponseEntity<List<PreguntaFrecuenteDTO>> getPreguntas(@PathVariable Long itemId) {
        return preguntaFrecuenteService.getPreguntasByItem(itemId);
    }

    @GetMapping("/{preguntaId}")
    public ResponseEntity<PreguntaFrecuenteDTO> getPregunta(@PathVariable Long preguntaId) {
        return preguntaFrecuenteService.getPregunta(preguntaId);
    }
}