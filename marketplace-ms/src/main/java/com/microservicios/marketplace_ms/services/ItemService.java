package com.microservicios.marketplace_ms.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.microservicios.marketplace_ms.dtos.ItemDTO;
import com.microservicios.marketplace_ms.dtos.ItemResponseDTO;
import com.microservicios.marketplace_ms.repositories.ClasificacionRepository;
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
    private ClasificacionRepository clasificacionRepository;

    @Autowired
    private ItemMapper itemMapper;

    public ResponseEntity<ItemDTO> createItemFromDTO(ItemDTO itemDTO) {
        // Validar que clasificacionId esté presente
        if (itemDTO.getClasificacionId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // Buscar la clasificación
        Optional<com.microservicios.marketplace_ms.entities.Clasificacion> clasificacionOpt = 
            clasificacionRepository.findById(itemDTO.getClasificacionId());
        if (clasificacionOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Crear entidad Item
        Item item = itemMapper.dtoToEntity(itemDTO);
        item.setClasificacion(clasificacionOpt.get());
        
        // Setear valores por defecto
        if (item.getFechaPublicacion() == null) {
            item.setFechaPublicacion(LocalDate.now());
        }
        if (item.getVisualizaciones() == null) {
            item.setVisualizaciones(0);
        }
        if (item.getCalificacionPromedio() == null) {
            item.setCalificacionPromedio(0L);
        }
        
        // Copiar datos de clasificación al item
        copyClasificacionDataToItem(item);
        
        Item saved = itemRepository.save(item);
        ItemDTO responseDTO = itemMapper.entityToDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
    
    public ResponseEntity<ItemDTO> updateItemFromDTO(Long id, ItemDTO itemDTO) {
        // Verificar que el item existe
        Optional<Item> existingItemOpt = itemRepository.findById(id);
        if (existingItemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Item existingItem = existingItemOpt.get();
        
        // Si se proporciona clasificacionId, validar y actualizar
        if (itemDTO.getClasificacionId() != null) {
            Optional<com.microservicios.marketplace_ms.entities.Clasificacion> clasificacionOpt = 
                clasificacionRepository.findById(itemDTO.getClasificacionId());
            if (clasificacionOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            existingItem.setClasificacion(clasificacionOpt.get());
            copyClasificacionDataToItem(existingItem);
        }
        
        // Actualizar campos modificables
        existingItem.setTitulo(itemDTO.getTitulo());
        existingItem.setDescripcion(itemDTO.getDescripcion());
        if (itemDTO.getStock() != null) {
            existingItem.setStock(itemDTO.getStock());
        }
        
        Item saved = itemRepository.save(existingItem);
        ItemDTO responseDTO = itemMapper.entityToDto(saved);
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<Item> createItem(Item item) {
        // Setear valores por defecto
        if (item.getFechaPublicacion() == null) {
            item.setFechaPublicacion(LocalDate.now());
        }
        if (item.getVisualizaciones() == null) {
            item.setVisualizaciones(0);
        }
        if (item.getCalificacionPromedio() == null) {
            item.setCalificacionPromedio(0L);
        }
        
        Item saved = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    private void copyClasificacionDataToItem(Item item) {
        if (item.getClasificacion() == null) return;
        
        // Copiar datos básicos de clasificación
        item.setLugarInicio(item.getClasificacion().getLugarInicio());
        item.setPrecio(item.getClasificacion().getPrecio());
        item.setFechaDisponibilidadInicio(item.getClasificacion().getFechaDisponibilidadInicio());
        item.setFechaDisponibilidadFin(item.getClasificacion().getFechaDisponibilidadFin());
        item.setCapacidadMaxima(item.getClasificacion().getCapacidadMaxima());
        
        // Manejar stock según tipo de clasificación
        String tipoClasificacion = item.getClasificacion().getClass().getSimpleName();
        if ("Alojamiento".equals(tipoClasificacion)) {
            item.setStock(null); // Alojamiento no usa stock
        } else if (item.getStock() == null) {
            // Para otros tipos, usar capacidadMaxima como stock inicial
            item.setStock(item.getClasificacion().getCapacidadMaxima());
        }
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
            boolean shouldDelete = false;
            if ("stock".equals(tipoCambio) && item.getStock() != null) {
                item.setStock(item.getStock() - cantidadVendida);
                itemRepository.save(item);
                System.out.println("Stock actualizado para item " + itemId + ": " + item.getStock());
                if (item.getStock() <= 0) {
                    shouldDelete = true;
                }
            } else if ("fechas".equals(tipoCambio) && item.getCapacidadMaxima() != null) {
                // Para alojamiento, reducir capacidad máxima (ej. habitaciones disponibles)
                item.setCapacidadMaxima(item.getCapacidadMaxima() - cantidadVendida);
                itemRepository.save(item);
                System.out.println("Capacidad actualizada para item " + itemId + ": " + item.getCapacidadMaxima());
                if (item.getCapacidadMaxima() <= 0) {
                    shouldDelete = true;
                }
            } else if ("cupo".equals(tipoCambio) && item.getCapacidadMaxima() != null) {
                item.setCapacidadMaxima(item.getCapacidadMaxima() - cantidadVendida);
                itemRepository.save(item);
                System.out.println("Cupo actualizado para item " + itemId + ": " + item.getCapacidadMaxima());
                if (item.getCapacidadMaxima() <= 0) {
                    shouldDelete = true;
                }
            }
            if (shouldDelete) {
                itemRepository.deleteById(itemId);
                System.out.println("Item " + itemId + " eliminado por agotamiento de stock/capacidad");
            }
        } else {
            System.out.println("Item no encontrado para actualizar: " + itemId);
        }
    }

}
