package com.microservicios.transaction_ms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.microservicios.transaction_ms.dtos.ItemCarritoDTO;
import com.microservicios.transaction_ms.dtos.UidRequestDTO;
import com.microservicios.transaction_ms.mappers.CarritoCompraMapper;
import com.microservicios.transaction_ms.mappers.ItemCarritoMapper;
import com.microservicios.transaction_ms.services.CarritoCompraService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/carrito-compra")
public class CarritoCompraController {
    @Autowired
    private CarritoCompraService carritoCompraService;

    @GetMapping("/list-items-carrito")
    public ResponseEntity<?> getAllItems(@RequestBody UidRequestDTO request) {
        String uid = request.getUid();
        List<ItemCarritoDTO> items = carritoCompraService.getCarritoItems(uid).stream()
                .map(ItemCarritoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/delete-item-carrito")
    public ResponseEntity<?> deleteItemFromCarrito(@RequestParam String uid,
            @RequestParam Long itemId) {
        boolean deleted = carritoCompraService.removeItemFromCarrito(uid, itemId) != null;
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("agregar-item-carrito")
    public ResponseEntity<?> addItemCarrito(@RequestParam String uid, @RequestBody ItemCarritoDTO itemCarritoDTO) {
        var itemCarrito = ItemCarritoMapper.toModel(itemCarritoDTO);
        var carritoActualizado = carritoCompraService.addItemToCarrito(uid, itemCarrito);
        return ResponseEntity.ok(CarritoCompraMapper.toDTO(carritoActualizado));
    }

}
