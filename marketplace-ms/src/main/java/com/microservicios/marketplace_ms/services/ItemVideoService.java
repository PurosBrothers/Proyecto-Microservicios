package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservicios.marketplace_ms.entities.ItemVideo;
import com.microservicios.marketplace_ms.repositories.ItemVideoRepository;

@Service
public class ItemVideoService {

    @Autowired
    private ItemVideoRepository itemVideoRepository;

    public ResponseEntity<ItemVideo> createItemVideo(ItemVideo itemVideo) {
        ItemVideo saved = itemVideoRepository.save(itemVideo);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public ResponseEntity<ItemVideo> getItemVideo(Long id) {
        Optional<ItemVideo> itemVideo = itemVideoRepository.findById(id);
        return itemVideo.map(v -> ResponseEntity.ok(v)).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<ItemVideo>> getAllItemVideos() {
        List<ItemVideo> itemVideos = itemVideoRepository.findAll();
        return ResponseEntity.ok(itemVideos);
    }

    public ResponseEntity<ItemVideo> updateItemVideo(Long id, ItemVideo itemVideo) {
        if (!itemVideoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemVideo.setId(id);
        ItemVideo updated = itemVideoRepository.save(itemVideo);
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<Void> deleteItemVideo(Long id) {
        if (!itemVideoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemVideoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}