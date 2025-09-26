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

import com.microservicios.marketplace_ms.entities.ItemVideo;
import com.microservicios.marketplace_ms.services.ItemVideoService;

@RestController
@RequestMapping("/itemvideos")
public class ItemVideoController {

    @Autowired
    private ItemVideoService itemVideoService;

    @PostMapping
    public ResponseEntity<ItemVideo> createItemVideo(@RequestBody ItemVideo itemVideo) {
        return itemVideoService.createItemVideo(itemVideo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemVideo> getItemVideo(@PathVariable Long id) {
        return itemVideoService.getItemVideo(id);
    }

    @GetMapping
    public ResponseEntity<List<ItemVideo>> getAllItemVideos() {
        return itemVideoService.getAllItemVideos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemVideo> updateItemVideo(@PathVariable Long id, @RequestBody ItemVideo itemVideo) {
        return itemVideoService.updateItemVideo(id, itemVideo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemVideo(@PathVariable Long id) {
        return itemVideoService.deleteItemVideo(id);
    }
}