package com.microservicios.marketplace_ms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicios.marketplace_ms.entities.ItemFoto;
import com.microservicios.marketplace_ms.services.ItemFotoService;

@RestController
@RequestMapping("/itemfotos")
public class ItemFotoController {

    @Autowired
    private ItemFotoService itemFotoService;

    @PostMapping
    public ResponseEntity<ItemFoto> createItemFoto(@RequestBody ItemFoto itemFoto) {
        return itemFotoService.createItemFoto(itemFoto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemFoto> getItemFoto(@PathVariable Long id) {
        return itemFotoService.getItemFoto(id);
    }

    @GetMapping
    public ResponseEntity<List<ItemFoto>> getAllItemFotos() {
        return itemFotoService.getAllItemFotos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemFoto> updateItemFoto(@PathVariable Long id, @RequestBody ItemFoto itemFoto) {
        return itemFotoService.updateItemFoto(id, itemFoto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemFoto(@PathVariable Long id) {
        return itemFotoService.deleteItemFoto(id);
    }
}