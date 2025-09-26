package com.microservicios.marketplace_ms.mappers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservicios.marketplace_ms.dtos.ItemDTO;
import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.repositories.ClasificacionRepository;

@Component
public class ItemMapper {

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    public Item dtoToEntity(ItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setTitulo(itemDTO.getTitulo());
        item.setDescripcion(itemDTO.getDescripcion());
        item.setFechaPublicacion(itemDTO.getFechaPublicacion());
        item.setStock(itemDTO.getStock());
        item.setVisualizaciones(itemDTO.getVisualizaciones());
        item.setCalificacionPromedio(itemDTO.getCalificacionPromedio());
        if (itemDTO.getClasificacionId() != null) {
            Optional<Clasificacion> clasificacion = clasificacionRepository.findById(itemDTO.getClasificacionId());
            clasificacion.ifPresent(item::setClasificacion);
        }
        return item;
    }

    public ItemDTO entityToDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setTitulo(item.getTitulo());
        itemDTO.setDescripcion(item.getDescripcion());
        itemDTO.setFechaPublicacion(item.getFechaPublicacion());
        itemDTO.setStock(item.getStock());
        itemDTO.setVisualizaciones(item.getVisualizaciones());
        itemDTO.setCalificacionPromedio(item.getCalificacionPromedio());
        if (item.getClasificacion() != null) {
            itemDTO.setClasificacionId(item.getClasificacion().getId());
        }
        return itemDTO;
    }
}
