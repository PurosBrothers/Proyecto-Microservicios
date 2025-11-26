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
        // ID is not set here; for create, it will be generated; for update, the service loads by ID
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
        
        // Mapear datos propios del Item (incluyendo datos de país)
        itemDTO.setLugarInicio(item.getLugarInicio());
        itemDTO.setPrecio(item.getPrecio());
        itemDTO.setFechaDisponibilidadInicio(item.getFechaDisponibilidadInicio());
        itemDTO.setFechaDisponibilidadFin(item.getFechaDisponibilidadFin());
        itemDTO.setCapacidadMaxima(item.getCapacidadMaxima());
        
        // Mapear datos de país del Item
        itemDTO.setPaisDestino(item.getPaisDestino());
        itemDTO.setFlag(item.getFlag());
        itemDTO.setPopulation(item.getPopulation());
        itemDTO.setGini(item.getGini());
        itemDTO.setFifa(item.getFifa());
        
        // Mapear Maps si existe
        if (item.getMaps() != null) {
            itemDTO.setMaps(new com.microservicios.marketplace_ms.dtos.MapsDTO(
                item.getMaps().getGoogleMaps(), 
                item.getMaps().getOpenStreetMaps()
            ));
        }
        
        // Mapear datos de clasificación (para casos donde aún se use la relación)
        if (item.getClasificacion() != null) {
            itemDTO.setClasificacionId(item.getClasificacion().getId());
            itemDTO.setUsuarioId(item.getClasificacion().getUsuarioId());
            itemDTO.setTipoClasificacion(item.getClasificacion().getTipo());
        }
        
        return itemDTO;
    }
    

}
