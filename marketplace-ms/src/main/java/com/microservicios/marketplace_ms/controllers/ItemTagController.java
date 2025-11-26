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

import com.microservicios.marketplace_ms.entities.ItemTag;
import com.microservicios.marketplace_ms.services.ItemTagService;

@RestController
@RequestMapping("/itemtags")
public class ItemTagController {

    @Autowired
    private ItemTagService itemTagService;

    @PostMapping
    public ResponseEntity<ItemTag> createItemTag(@RequestBody ItemTag itemTag) {
        return itemTagService.createItemTag(itemTag);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemTag> getItemTag(@PathVariable Long id) {
        return itemTagService.getItemTag(id);
    }

    @GetMapping
    public ResponseEntity<List<ItemTag>> getAllItemTags() {
        return itemTagService.getAllItemTags();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemTag> updateItemTag(@PathVariable Long id, @RequestBody ItemTag itemTag) {
        return itemTagService.updateItemTag(id, itemTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemTag(@PathVariable Long id) {
        return itemTagService.deleteItemTag(id);
    }
}