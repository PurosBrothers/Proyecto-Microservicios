package com.microservicios.marketplace_ms.mappers;

import org.springframework.stereotype.Component;

import com.microservicios.marketplace_ms.dtos.PreguntaFrecuenteDTO;
import com.microservicios.marketplace_ms.entities.PreguntaFrecuente;

@Component
public class PreguntaFrecuenteMapper {

    public PreguntaFrecuente dtoToEntity(PreguntaFrecuenteDTO dto) {
        if (dto == null) {
            return null;
        }
        PreguntaFrecuente entity = new PreguntaFrecuente();
        entity.setId(dto.getId());
        entity.setPregunta(dto.getPregunta());
        // Note: item relationship will be set in service
        return entity;
    }

    public PreguntaFrecuenteDTO entityToDto(PreguntaFrecuente entity) {
        if (entity == null) {
            return null;
        }
        PreguntaFrecuenteDTO dto = new PreguntaFrecuenteDTO();
        dto.setId(entity.getId());
        dto.setPregunta(entity.getPregunta());
        if (entity.getItem() != null) {
            dto.setItemId(entity.getItem().getId());
        }
        return dto;
    }
}