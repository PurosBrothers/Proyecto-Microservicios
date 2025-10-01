package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.microservicios.marketplace_ms.dtos.ItemDTO;
import com.microservicios.marketplace_ms.dtos.ItemResponseDTO;
import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.entities.Alimentacion;
import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.entities.PaseosEcologicos;
import com.microservicios.marketplace_ms.entities.Transporte;
import com.microservicios.marketplace_ms.mappers.ItemMapper;
import com.microservicios.marketplace_ms.repositories.ItemRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    public ResponseEntity<Item> createItem(Item item) {
        Item saved = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public ResponseEntity<ItemResponseDTO> getItem(Long id) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Item item = itemOpt.get();
        ItemDTO itemDTO = itemMapper.entityToDto(item);

        ItemResponseDTO response = new ItemResponseDTO();
        response.setItem(itemDTO);

        response.setClasificacionData(item.getClasificacion());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Item> updateItem(Long id, Item item) {
        if (!itemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
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

    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
        List<Item> allItems = itemRepository.findAll();
        List<ItemResponseDTO> results = allItems.stream()
                .map(item -> {
                    ItemDTO itemDTO = itemMapper.entityToDto(item);
                    ItemResponseDTO response = new ItemResponseDTO();
                    response.setItem(itemDTO);
                    response.setClasificacionData(item.getClasificacion());
                    return response;
                })
                .toList();
        return ResponseEntity.ok(results);
    }

    public ResponseEntity<List<ItemResponseDTO>> searchItems(String query) {
        List<Item> allItems = itemRepository.findAll();
        List<ItemResponseDTO> results = allItems.stream()
                .filter(item -> item.getTitulo().toLowerCase().contains(query.toLowerCase()) ||
                        item.getDescripcion().toLowerCase().contains(query.toLowerCase()) ||
                        item.getTags().stream()
                                .anyMatch(tag -> tag.getTag().toLowerCase().contains(query.toLowerCase())))
                .map(item -> {
                    ItemDTO itemDTO = itemMapper.entityToDto(item);
                    ItemResponseDTO response = new ItemResponseDTO();
                    response.setItem(itemDTO);
                    response.setClasificacionData(item.getClasificacion());
                    return response;
                })
                .toList();
        return ResponseEntity.ok(results);
    }

    public List<Item> getItemsPorClasificacion(String clasificacion) {
        System.out.println("Buscando items por clasificación: " + clasificacion);
        Class<?> clasificacionClass = switch (clasificacion) {
            case "Alojamiento" -> Alojamiento.class;
            case "Alimentacion" -> Alimentacion.class;
            case "PaseosEcologicos" -> PaseosEcologicos.class;
            case "Transporte" -> Transporte.class;
            default -> null;
        };
        if (clasificacionClass == null) {
            System.out.println("Clasificación no válida: " + clasificacion);
            return List.of();
        }
        System.out.println("Clase de clasificación: " + clasificacionClass.getSimpleName());
        List<Item> items = itemRepository.findByClasificacionType(clasificacionClass);
        System.out.println("Encontrados " + items.size() + " items");
        return items;
    }

    public void updateItemStock(Long itemId, Integer cantidadVendida, String tipoCambio) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            String clasificacionTipo = item.getClasificacion().getTipo();
            if ("Alojamiento".equals(clasificacionTipo)) {
                // Para alojamiento, no usar stock, usar capacidadMaxima
                if ("cupo".equals(tipoCambio) && item.getCapacidadMaxima() != null) {
                    item.setCapacidadMaxima(item.getCapacidadMaxima() - cantidadVendida);
                    itemRepository.save(item);
                    System.out.println("Cupo actualizado para alojamiento " + itemId + ": " + item.getCapacidadMaxima());
                } else if ("fechas".equals(tipoCambio)) {
                    // Marcar fechas como reservadas
                    System.out.println("Actualización de fechas para alojamiento " + itemId);
                }
            } else {
                // Para otras clasificaciones, usar stock
                if ("stock".equals(tipoCambio) && item.getStock() != null) {
                    item.setStock(item.getStock() - cantidadVendida);
                    itemRepository.save(item);
                    System.out.println("Stock actualizado para item " + itemId + ": " + item.getStock());
                }
            }
        } else {
            System.out.println("Item no encontrado para actualizar: " + itemId);
        }
    }

}
