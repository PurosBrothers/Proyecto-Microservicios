package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservicios.marketplace_ms.entities.ItemFoto;
import com.microservicios.marketplace_ms.repositories.ItemFotoRepository;

@Service
public class ItemFotoService {

    @Autowired
    private ItemFotoRepository itemFotoRepository;

    public ResponseEntity<ItemFoto> createItemFoto(ItemFoto itemFoto) {
        ItemFoto saved = itemFotoRepository.save(itemFoto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public ResponseEntity<ItemFoto> getItemFoto(Long id) {
        Optional<ItemFoto> itemFoto = itemFotoRepository.findById(id);
        return itemFoto.map(f -> ResponseEntity.ok(f)).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<ItemFoto>> getAllItemFotos() {
        List<ItemFoto> itemFotos = itemFotoRepository.findAll();
        return ResponseEntity.ok(itemFotos);
    }

    public ResponseEntity<ItemFoto> updateItemFoto(Long id, ItemFoto itemFoto) {
        if (!itemFotoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemFoto.setId(id);
        ItemFoto updated = itemFotoRepository.save(itemFoto);
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<Void> deleteItemFoto(Long id) {
        if (!itemFotoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemFotoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}