package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservicios.marketplace_ms.dtos.PreguntaFrecuenteDTO;
import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.entities.PreguntaFrecuente;
import com.microservicios.marketplace_ms.mappers.PreguntaFrecuenteMapper;
import com.microservicios.marketplace_ms.repositories.ItemRepository;
import com.microservicios.marketplace_ms.repositories.PreguntaFrecuenteRepository;

@Service
public class PreguntaFrecuenteService {

    @Autowired
    private PreguntaFrecuenteRepository preguntaFrecuenteRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PreguntaFrecuenteMapper preguntaFrecuenteMapper;

    public ResponseEntity<PreguntaFrecuenteDTO> addPreguntaToItem(Long itemId, PreguntaFrecuenteDTO preguntaDTO) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Item item = itemOpt.get();
        PreguntaFrecuente pregunta = preguntaFrecuenteMapper.dtoToEntity(preguntaDTO);
        pregunta.setItem(item);

        PreguntaFrecuente saved = preguntaFrecuenteRepository.save(pregunta);
        PreguntaFrecuenteDTO response = preguntaFrecuenteMapper.entityToDto(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<Void> removePreguntaFromItem(Long itemId, Long preguntaId) {
        Optional<PreguntaFrecuente> preguntaOpt = preguntaFrecuenteRepository.findById(preguntaId);
        if (preguntaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PreguntaFrecuente pregunta = preguntaOpt.get();
        if (!pregunta.getItem().getId().equals(itemId)) {
            return ResponseEntity.badRequest().build(); // Pregunta no pertenece al item
        }

        preguntaFrecuenteRepository.delete(pregunta);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<PreguntaFrecuenteDTO>> getPreguntasByItem(Long itemId) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Item item = itemOpt.get();
        List<PreguntaFrecuenteDTO> preguntas = item.getPreguntasFrecuentes().stream()
                .map(preguntaFrecuenteMapper::entityToDto)
                .toList();

        return ResponseEntity.ok(preguntas);
    }

    public ResponseEntity<PreguntaFrecuenteDTO> getPregunta(Long id) {
        Optional<PreguntaFrecuente> preguntaOpt = preguntaFrecuenteRepository.findById(id);
        if (preguntaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        PreguntaFrecuenteDTO dto = preguntaFrecuenteMapper.entityToDto(preguntaOpt.get());
        return ResponseEntity.ok(dto);
    }
}