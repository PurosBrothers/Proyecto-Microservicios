package com.microservicios.marketplace_ms.mappers;

import com.microservicios.marketplace_ms.dtos.RestriccionesDieteticasDTO;
import com.microservicios.marketplace_ms.entities.RestriccionesDieteticas;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestriccionesDieteticasMapper {

    public RestriccionesDieteticasDTO toDto(RestriccionesDieteticas entity) {
        if (entity == null) {
            return null;
        }
        
        RestriccionesDieteticasDTO dto = new RestriccionesDieteticasDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        
        return dto;
    }

    public RestriccionesDieteticas toEntity(RestriccionesDieteticasDTO dto) {
        if (dto == null) {
            return null;
        }
        
        RestriccionesDieteticas entity = new RestriccionesDieteticas();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        
        return entity;
    }

    public List<RestriccionesDieteticasDTO> toDtoList(List<RestriccionesDieteticas> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RestriccionesDieteticas> toEntityList(List<RestriccionesDieteticasDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}