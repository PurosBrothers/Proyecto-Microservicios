package com.microservicios.transaction_ms.controllers;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.microservicios.transaction_ms.dtos.TransaccionDTO;
import com.microservicios.transaction_ms.mappers.TransaccionMapper;
import com.microservicios.transaction_ms.models.Transaccion;
import com.microservicios.transaction_ms.services.TransaccionService;
import com.netflix.discovery.converters.Auto;

@Controller
@RequestMapping("/transaccion")
public class TransaccionController {
    @Autowired
    private TransaccionService transaccionService;

    @GetMapping("/list/{uid}")
    public ResponseEntity<?> getAllTransacciones(@PathVariable String uid) {
        List<TransaccionDTO> transacciones = transaccionService.getTransacciones(uid).stream()
                .map(TransaccionMapper::toDTO)
                .toList();
        return ResponseEntity.ok(transacciones);
    }

    @PostMapping("/create/{uid}")
    public ResponseEntity<?> createTransaccion(@PathVariable String uid,
            @RequestBody(required = false) List<Long> itemIds) {
        Transaccion transaccion = transaccionService.createTransactionFromCarrito(uid, itemIds);
        if (transaccion != null) {
            return ResponseEntity.ok(transaccion);
        } else {
            return ResponseEntity.badRequest().body("No hay items válidos en el carrito para procesar");
        }
    }

    @PostMapping("/process-payment/{uid}")
    public ResponseEntity<?> processPayment(@PathVariable String uid, @RequestBody List<Long> itemIds) {
        // Crear transacción desde carrito
        Transaccion transaccion = transaccionService.createTransactionFromCarrito(uid, itemIds);
        if (transaccion == null) {
            return ResponseEntity.badRequest().body("No hay items válidos en el carrito para procesar");
        }
        // Procesar pago
        transaccionService.processPayment(transaccion.getId());
        return ResponseEntity.ok("Transacción creada y mensaje de procesamiento de pago enviado");
    }

}
