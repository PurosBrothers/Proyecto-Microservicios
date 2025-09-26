package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservicios.marketplace_ms.entities.ItemLink;
import com.microservicios.marketplace_ms.repositories.ItemLinkRepository;

@Service
public class ItemLinkService {

    @Autowired
    private ItemLinkRepository itemLinkRepository;

    public ResponseEntity<ItemLink> createItemLink(ItemLink itemLink) {
        ItemLink saved = itemLinkRepository.save(itemLink);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public ResponseEntity<ItemLink> getItemLink(Long id) {
        Optional<ItemLink> itemLink = itemLinkRepository.findById(id);
        return itemLink.map(l -> ResponseEntity.ok(l)).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<ItemLink>> getAllItemLinks() {
        List<ItemLink> itemLinks = itemLinkRepository.findAll();
        return ResponseEntity.ok(itemLinks);
    }

    public ResponseEntity<ItemLink> updateItemLink(Long id, ItemLink itemLink) {
        if (!itemLinkRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemLink.setId(id);
        ItemLink updated = itemLinkRepository.save(itemLink);
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<Void> deleteItemLink(Long id) {
        if (!itemLinkRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemLinkRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}