package com.microservicios.marketplace_ms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.mappers.ItemMapper;
import com.microservicios.marketplace_ms.messagingrabbitmq.components.RabbitMQSender;
import com.microservicios.marketplace_ms.services.ItemService;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO item) {
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
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDTO item) {
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

    @PostMapping("/{id}/add-to-cart")
    public ResponseEntity<String> addToCart(@PathVariable Long id, @Valid @RequestBody AddToCartMessageDTO request) {
        // Validar item existe
        ResponseEntity<ItemResponseDTO> itemResponse = itemService.getItem(id);
        if (!itemResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.badRequest().body("Item no encontrado");
        }

        // Enviar mensaje a transaction-ms
        String tipoClasificacion = itemResponse.getBody().getItem().getClasificacion().getClass().getSimpleName();
        String nombreItem = itemResponse.getBody().getItem().getTitulo();
        AddToCartMessageDTO message = new AddToCartMessageDTO(
                request.getUid(),
                id,
                request.getCantidad(),
                request.getPrecioUnitario(),
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
