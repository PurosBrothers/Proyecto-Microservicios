package com.microservicios.marketplace_ms.mappers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservicios.marketplace_ms.dtos.ItemDTO;
import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.entities.Alimentacion;
import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.entities.PaseosEcologicos;
import com.microservicios.marketplace_ms.entities.Transporte;
import com.microservicios.marketplace_ms.repositories.AlojamientoRepository;
import com.microservicios.marketplace_ms.repositories.AlimentacionRepository;
import com.microservicios.marketplace_ms.repositories.PaseosEcologicosRepository;
import com.microservicios.marketplace_ms.repositories.TransporteRepository;

@Component
public class ItemMapper {

    @Autowired
    private AlojamientoRepository alojamientoRepository;

    @Autowired
    private AlimentacionRepository alimentacionRepository;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private PaseosEcologicosRepository paseosEcologicosRepository;

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
        item.setClasificationType(itemDTO.getClasificationType());
        item.setClasificacionId(itemDTO.getClasificacionId());
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
        itemDTO.setClasificationType(item.getClasificationType());
        itemDTO.setClasificacionId(item.getClasificacionId());
        return itemDTO;
    }
}
