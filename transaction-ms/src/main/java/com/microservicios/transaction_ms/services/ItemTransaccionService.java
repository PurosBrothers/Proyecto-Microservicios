package com.microservicios.transaction_ms.services;

import com.microservicios.transaction_ms.models.ItemTransaccion;
import com.microservicios.transaction_ms.repository.ItemTransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemTransaccionService {

    @Autowired
    private ItemTransaccionRepository repository;

    public ItemTransaccion create(ItemTransaccion itemTransaccion) {
        return repository.save(itemTransaccion);
    }

    public ItemTransaccion update(Long id, ItemTransaccion itemTransaccion) {
        itemTransaccion.setId(id);
        return repository.save(itemTransaccion);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<ItemTransaccion> findAll() {
        return repository.findAll();
    }

    public Optional<ItemTransaccion> findById(Long id) {
        return repository.findById(id);
    }
}