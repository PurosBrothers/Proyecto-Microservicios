package com.microservicios.transaction_ms.controllers;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.microservicios.transaction_ms.dtos.ItemTransaccionDTO;
import com.microservicios.transaction_ms.mappers.ItemTransaccionMapper;
import com.microservicios.transaction_ms.models.Transaccion;
import com.microservicios.transaction_ms.services.TransaccionService;
import com.netflix.discovery.converters.Auto;

@Controller
@RequestMapping("/transaccion")
public class TransaccionController {
    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private ItemTransaccionMapper itemTransaccionMapper;

    @GetMapping("/list/{uid}")
    public ResponseEntity<?> getAllTransacciones(@PathVariable String uid) {
        List<ItemTransaccionDTO> items = transaccionService.getItemsTransaccion(uid).stream()
                .map(ItemTransaccionMapper::toDTO)
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/create/{uid}")
    public ResponseEntity<?> createTransaccion(@PathVariable String uid) {
        Transaccion transaccion = transaccionService.createTransactionFromCarrito(uid);
        if (transaccion != null) {
            return ResponseEntity.ok(transaccion);
        } else {
            return ResponseEntity.badRequest().body("No hay items en el carrito");
        }
    }

}
