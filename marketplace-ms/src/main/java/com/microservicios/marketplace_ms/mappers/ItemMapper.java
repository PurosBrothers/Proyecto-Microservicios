package com.microservicios.marketplace_ms.mappers;

import org.springframework.stereotype.Component;

import com.microservicios.marketplace_ms.dtos.ItemDTO;
import com.microservicios.marketplace_ms.entities.Item;

@Component
public class ItemMapper {



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
        
        // El servicio se encargará de cargar la clasificación usando clasificacionId
        
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
        
        // Mapear datos de clasificación al nuevo DTO
        if (item.getClasificacion() != null) {
            itemDTO.setClasificacionId(item.getClasificacion().getId());
            itemDTO.setLugarInicio(item.getClasificacion().getLugarInicio());
            itemDTO.setPrecio(item.getClasificacion().getPrecio());
            itemDTO.setFechaDisponibilidadInicio(item.getClasificacion().getFechaDisponibilidadInicio());
            itemDTO.setFechaDisponibilidadFin(item.getClasificacion().getFechaDisponibilidadFin());
            itemDTO.setCapacidadMaxima(item.getClasificacion().getCapacidadMaxima());
            itemDTO.setUsuarioId(item.getClasificacion().getUsuarioId());
        }
        
        return itemDTO;
    }
    

}
