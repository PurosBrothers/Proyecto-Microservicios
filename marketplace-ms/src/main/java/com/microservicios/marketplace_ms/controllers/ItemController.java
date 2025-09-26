package com.microservicios.marketplace_ms.controllers;

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
import com.microservicios.marketplace_ms.dtos.ItemDTO;
import com.microservicios.marketplace_ms.dtos.ItemResponseDTO;
import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.mappers.ItemMapper;
import com.microservicios.marketplace_ms.services.ItemService;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemMapper itemMapper;

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO item) {
        Item entity = itemMapper.dtoToEntity(item);
        ResponseEntity<Item> response = itemService.createItem(entity);
        if (response.getStatusCode().is2xxSuccessful()) {
            ItemDTO dto = itemMapper.entityToDto(response.getBody());
            return ResponseEntity.status(response.getStatusCode()).body(dto);
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItem(@PathVariable Long id) {
        ResponseEntity<ItemResponseDTO> response = itemService.getItem(id);
        return response;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @RequestBody ItemDTO item) {
        Item entity = itemMapper.dtoToEntity(item);
        ResponseEntity<Item> response = itemService.updateItem(id, entity);
        if (response.getStatusCode().is2xxSuccessful()) {
            ItemDTO dto = itemMapper.entityToDto(response.getBody());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @GetMapping("/{id}/clasificacion")
    public ResponseEntity<Object> getItemClasificacion(@PathVariable Long id) {
        ResponseEntity<ItemResponseDTO> response = itemService.getItem(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody().getClasificacionData());
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id);
    }
}
