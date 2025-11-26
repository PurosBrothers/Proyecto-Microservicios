package com.microservicios.marketplace_ms.mappers;

import com.microservicios.marketplace_ms.dtos.RequisitosEspecialesDTO;
import com.microservicios.marketplace_ms.entities.RequisitosEspeciales;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequisitosEspecialesMapper {

    public RequisitosEspecialesDTO toDto(RequisitosEspeciales entity) {
        if (entity == null) {
            return null;
        }
        
        RequisitosEspecialesDTO dto = new RequisitosEspecialesDTO();
        dto.setId(entity.getId());
        dto.setRequisito(entity.getRequisito());
        
        return dto;
    }

    public RequisitosEspeciales toEntity(RequisitosEspecialesDTO dto) {
        if (dto == null) {
            return null;
        }
        
        RequisitosEspeciales entity = new RequisitosEspeciales();
        entity.setId(dto.getId());
        entity.setRequisito(dto.getRequisito());
        
        return entity;
    }

    public List<RequisitosEspecialesDTO> toDtoList(List<RequisitosEspeciales> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RequisitosEspeciales> toEntityList(List<RequisitosEspecialesDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}