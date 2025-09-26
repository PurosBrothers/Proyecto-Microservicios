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

import com.microservicios.marketplace_ms.entities.ItemLink;
import com.microservicios.marketplace_ms.services.ItemLinkService;

@RestController
@RequestMapping("/itemlinks")
public class ItemLinkController {

    @Autowired
    private ItemLinkService itemLinkService;

    @PostMapping
    public ResponseEntity<ItemLink> createItemLink(@RequestBody ItemLink itemLink) {
        return itemLinkService.createItemLink(itemLink);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemLink> getItemLink(@PathVariable Long id) {
        return itemLinkService.getItemLink(id);
    }

    @GetMapping
    public ResponseEntity<List<ItemLink>> getAllItemLinks() {
        return itemLinkService.getAllItemLinks();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemLink> updateItemLink(@PathVariable Long id, @RequestBody ItemLink itemLink) {
        return itemLinkService.updateItemLink(id, itemLink);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemLink(@PathVariable Long id) {
        return itemLinkService.deleteItemLink(id);
    }
}