package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservicios.marketplace_ms.entities.ItemTag;
import com.microservicios.marketplace_ms.repositories.ItemTagRepository;

@Service
public class ItemTagService {

    @Autowired
    private ItemTagRepository itemTagRepository;

    public ResponseEntity<ItemTag> createItemTag(ItemTag itemTag) {
        ItemTag saved = itemTagRepository.save(itemTag);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public ResponseEntity<ItemTag> getItemTag(Long id) {
        Optional<ItemTag> itemTag = itemTagRepository.findById(id);
        return itemTag.map(t -> ResponseEntity.ok(t)).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<ItemTag>> getAllItemTags() {
        List<ItemTag> itemTags = itemTagRepository.findAll();
        return ResponseEntity.ok(itemTags);
    }

    public ResponseEntity<ItemTag> updateItemTag(Long id, ItemTag itemTag) {
        if (!itemTagRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemTag.setId(id);
        ItemTag updated = itemTagRepository.save(itemTag);
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<Void> deleteItemTag(Long id) {
        if (!itemTagRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemTagRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}