package com.microservicios.transaction_ms.services;

import com.microservicios.transaction_ms.models.ItemCarrito;
import com.microservicios.transaction_ms.repository.ItemCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemCarritoService {

    @Autowired
    private ItemCarritoRepository repository;

    public ItemCarrito create(ItemCarrito itemCarrito) {
        return repository.save(itemCarrito);
    }

    public ItemCarrito update(Long id, ItemCarrito itemCarrito) {
        itemCarrito.setId(id);
        return repository.save(itemCarrito);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<ItemCarrito> findAll() {
        return repository.findAll();
    }

    public Optional<ItemCarrito> findById(Long id) {
        return repository.findById(id);
    }
}