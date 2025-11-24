package com.microservicios.marketplace_ms.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;
import com.microservicios.marketplace_ms.dtos.AddToCartMessageDTO;
import com.microservicios.marketplace_ms.dtos.ItemDTO;
import com.microservicios.marketplace_ms.dtos.ItemResponseDTO;
import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.entities.Alimentacion;
import com.microservicios.marketplace_ms.entities.PaseosEcologicos;
import com.microservicios.marketplace_ms.entities.Transporte;
import com.microservicios.marketplace_ms.messagingrabbitmq.components.RabbitMQSender;
import com.microservicios.marketplace_ms.services.ItemService;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @PostMapping
    @PreAuthorize("hasRole('PROVEEDOR')")
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO) {
        ResponseEntity<ItemDTO> response = itemService.createItemFromDTO(itemDTO);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItem(@PathVariable Long id) {
        ResponseEntity<ItemResponseDTO> response = itemService.getItem(id);
        return response;
    }

    // Endpoint interno para comunicación entre microservicios (sin autenticación)
    @GetMapping("/internal/{id}")
    public ResponseEntity<ItemResponseDTO> getItemInternal(@PathVariable Long id) {
        ResponseEntity<ItemResponseDTO> response = itemService.getItem(id);
        return response;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROVEEDOR')")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDTO itemDTO) {
        return itemService.updateItemFromDTO(id, itemDTO);
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
    @PreAuthorize("hasRole('PROVEEDOR')")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id);
    }

    @PostMapping("/{id}/add-to-cart")
    public ResponseEntity<String> addToCart(@PathVariable Long id, @Valid @RequestBody AddToCartMessageDTO request) {
        // Validar item existe
        ResponseEntity<ItemResponseDTO> itemResponse = itemService.getItem(id);
        if (!itemResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.badRequest().body("Item no encontrado");
        }

        // Obtener precio del item si no viene en el request
        BigDecimal precioUnitario = request.getPrecioUnitario();
        if (precioUnitario == null) {
            Object clasificacion = itemResponse.getBody().getClasificacionData();
            if (clasificacion instanceof Alojamiento) {
                precioUnitario = ((Alojamiento) clasificacion).getPrecio();
            } else if (clasificacion instanceof Alimentacion) {
                precioUnitario = ((Alimentacion) clasificacion).getPrecio();
            } else if (clasificacion instanceof Transporte) {
                precioUnitario = ((Transporte) clasificacion).getPrecio();
            } else if (clasificacion instanceof PaseosEcologicos) {
                precioUnitario = ((PaseosEcologicos) clasificacion).getPrecio();
            }
        }

        // Enviar mensaje a transaction-ms
        Object clasificacionData = itemResponse.getBody().getClasificacionData();
        String tipoClasificacion = clasificacionData.getClass().getSimpleName();
        String nombreItem = itemResponse.getBody().getItem().getTitulo();
        AddToCartMessageDTO message = new AddToCartMessageDTO(
                request.getUid(),
                id,
                request.getCantidad(),
                precioUnitario,
                tipoClasificacion,
                nombreItem);
        rabbitMQSender.sendAddToCartMessage(message);

        return ResponseEntity.ok("Mensaje enviado para agregar al carrito");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseDTO>> searchItems(@RequestParam String query) {
        ResponseEntity<List<ItemResponseDTO>> response = itemService.searchItems(query);
        return response;
    }

    @GetMapping()
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
        ResponseEntity<List<ItemResponseDTO>> response = itemService.getAllItems();
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder("Errores de validación: ");
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.append(fieldName).append(": ").append(errorMessage).append("; ");
        });
        return ResponseEntity.badRequest().body(errors.toString());
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class, ServerWebInputException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDeserializationExceptions(Exception ex) {
        return ResponseEntity.badRequest().body("Error al procesar la solicitud: " + ex.getMessage());
    }
}
