package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.repositories.ClasificacionRepository;
import com.microservicios.marketplace_ms.repositories.ItemRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    public ResponseEntity<Item> createItem(Item item) {
        Item saved = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public ResponseEntity<Item> getItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.map(i -> ResponseEntity.ok(i)).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Item> updateItem(Long id, Item item) {
        if (!itemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (item.getClasificacion() != null && item.getClasificacion().getId() == null) {
            Clasificacion savedClasificacion = clasificacionRepository.save(item.getClasificacion());
            item.setClasificacion(savedClasificacion);
        }
        item.setId(id);
        Item updated = itemRepository.save(item);
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<Void> deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return ResponseEntity.ok(items);
    }
}
