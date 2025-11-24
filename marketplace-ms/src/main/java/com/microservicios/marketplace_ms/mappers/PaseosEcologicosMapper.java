package com.microservicios.marketplace_ms.mappers;

import com.microservicios.marketplace_ms.dtos.PaseosEcologicosDTO;
import com.microservicios.marketplace_ms.entities.PaseosEcologicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaseosEcologicosMapper {

    @Autowired
    private RequisitosEspecialesMapper requisitosEspecialesMapper;

    public PaseosEcologicosDTO toDto(PaseosEcologicos entity) {
        if (entity == null) {
            return null;
        }
        
        PaseosEcologicosDTO dto = new PaseosEcologicosDTO();
        
        // Mapear campos de la clase padre
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo());
        dto.setLugarInicio(entity.getLugarInicio());
        dto.setPrecio(entity.getPrecio());
        dto.setFechaDisponibilidadInicio(entity.getFechaDisponibilidadInicio());
        dto.setFechaDisponibilidadFin(entity.getFechaDisponibilidadFin());
        dto.setCapacidadMaxima(entity.getCapacidadMaxima());
        dto.setUsuarioId(entity.getUsuarioId());
        dto.setRequisitosEspeciales(requisitosEspecialesMapper.toDtoList(entity.getRequisitosEspeciales()));
        
        // Mapear campos específicos de PaseosEcologicos
        dto.setDuracionHoras(entity.getDuracionHoras());
        dto.setNivelDificultad(entity.getNivelDificultad());
        dto.setEquipoIncluido(entity.getEquipoIncluido());
        dto.setGuiaIncluido(entity.getGuiaIncluido());
        dto.setEdadMinima(entity.getEdadMinima());
        dto.setPuntoEncuentro(entity.getPuntoEncuentro());
        dto.setRutaEncuentro(entity.getRutaEncuentro());
        
        return dto;
    }

    public PaseosEcologicos toEntity(PaseosEcologicosDTO dto) {
        if (dto == null) {
            return null;
        }
        
        PaseosEcologicos entity = new PaseosEcologicos();
        
        // Mapear campos de la clase padre
        entity.setId(dto.getId());
        entity.setTipo(dto.getTipo());
        entity.setLugarInicio(dto.getLugarInicio());
        entity.setPrecio(dto.getPrecio());
        entity.setFechaDisponibilidadInicio(dto.getFechaDisponibilidadInicio());
        entity.setFechaDisponibilidadFin(dto.getFechaDisponibilidadFin());
        entity.setCapacidadMaxima(dto.getCapacidadMaxima());
        entity.setUsuarioId(dto.getUsuarioId());
        entity.setRequisitosEspeciales(requisitosEspecialesMapper.toEntityList(dto.getRequisitosEspeciales()));
        
        // Mapear campos específicos de PaseosEcologicos
        entity.setDuracionHoras(dto.getDuracionHoras());
        entity.setNivelDificultad(dto.getNivelDificultad());
        entity.setEquipoIncluido(dto.getEquipoIncluido());
        entity.setGuiaIncluido(dto.getGuiaIncluido());
        entity.setEdadMinima(dto.getEdadMinima());
        entity.setPuntoEncuentro(dto.getPuntoEncuentro());
        entity.setRutaEncuentro(dto.getRutaEncuentro());
        
        return entity;
    }
}