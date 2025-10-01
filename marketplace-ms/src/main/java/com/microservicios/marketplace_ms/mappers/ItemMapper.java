package com.microservicios.marketplace_ms.mappers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservicios.marketplace_ms.dtos.ItemDTO;
import com.microservicios.marketplace_ms.entities.Item;

@Component
public class ItemMapper {

    @Autowired
    private PreguntaFrecuenteMapper preguntaFrecuenteMapper;

    public Item dtoToEntity(ItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setTitulo(itemDTO.getTitulo());
        item.setDescripcion(itemDTO.getDescripcion());
        item.setFechaPublicacion(itemDTO.getFechaPublicacion());
        // For alojamiento, stock is not applicable, for others stock = capacidadMaxima
        if (itemDTO.getClasificacion() != null && "Alojamiento".equals(itemDTO.getClasificacion().getTipo())) {
            item.setStock(null);
        } else {
            item.setStock(itemDTO.getStock() != null ? itemDTO.getStock() : itemDTO.getClasificacion().getCapacidadMaxima());
        }
        item.setVisualizaciones(itemDTO.getVisualizaciones());
        item.setCalificacionPromedio(itemDTO.getCalificacionPromedio());
        item.setClasificacion(itemDTO.getClasificacion());
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
        itemDTO.setClasificacion(item.getClasificacion());
        if (item.getPreguntasFrecuentes() != null) {
            itemDTO.setPreguntasFrecuentes(item.getPreguntasFrecuentes().stream()
                    .map(preguntaFrecuenteMapper::entityToDto)
                    .collect(Collectors.toList()));
        }
        return itemDTO;
    }
}
