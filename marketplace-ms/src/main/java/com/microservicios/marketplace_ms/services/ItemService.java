package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;
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
import com.microservicios.marketplace_ms.repositories.AlojamientoRepository;
import com.microservicios.marketplace_ms.repositories.AlimentacionRepository;
import com.microservicios.marketplace_ms.repositories.ItemRepository;
import com.microservicios.marketplace_ms.repositories.PaseosEcologicosRepository;
import com.microservicios.marketplace_ms.repositories.TransporteRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AlojamientoRepository alojamientoRepository;

    @Autowired
    private AlimentacionRepository alimentacionRepository;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private PaseosEcologicosRepository paseosEcologicosRepository;

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

        // Fetch classification data based on type
        Object clasificacionData = null;
        if ("alojamiento".equals(item.getClasificationType()) && item.getClasificacionId() != null) {
            Optional<Alojamiento> alojamiento = alojamientoRepository.findById(item.getClasificacionId());
            clasificacionData = alojamiento.orElse(null);
        } else if ("alimentacion".equals(item.getClasificationType()) && item.getClasificacionId() != null) {
            Optional<Alimentacion> alimentacion = alimentacionRepository.findById(item.getClasificacionId());
            clasificacionData = alimentacion.orElse(null);
        } else if ("transporte".equals(item.getClasificationType()) && item.getClasificacionId() != null) {
            Optional<Transporte> transporte = transporteRepository.findById(item.getClasificacionId());
            clasificacionData = transporte.orElse(null);
        } else if ("paseos-ecologicos".equals(item.getClasificationType()) && item.getClasificacionId() != null) {
            Optional<PaseosEcologicos> paseos = paseosEcologicosRepository.findById(item.getClasificacionId());
            clasificacionData = paseos.orElse(null);
        }

        response.setClasificacionData(clasificacionData);
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

    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return ResponseEntity.ok(items);
    }
}
