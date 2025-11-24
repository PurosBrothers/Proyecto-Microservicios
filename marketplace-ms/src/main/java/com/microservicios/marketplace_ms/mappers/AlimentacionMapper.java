package com.microservicios.marketplace_ms.mappers;

import com.microservicios.marketplace_ms.dtos.AlimentacionDTO;
import com.microservicios.marketplace_ms.entities.Alimentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlimentacionMapper {

    @Autowired
    private RequisitosEspecialesMapper requisitosEspecialesMapper;
    
    @Autowired
    private RestriccionesDieteticasMapper restriccionesDieteticasMapper;

    public AlimentacionDTO toDto(Alimentacion entity) {
        if (entity == null) {
            return null;
        }
        
        AlimentacionDTO dto = new AlimentacionDTO();
        
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
        
        // Mapear campos específicos de Alimentacion
        dto.setHoraInicio(entity.getHoraInicio());
        dto.setHoraFinal(entity.getHoraFinal());
        dto.setTipoComida(entity.getTipoComida());
        dto.setMenuIncluido(entity.getMenuIncluido());
        dto.setLatitud(entity.getLatitud());
        dto.setLongitud(entity.getLongitud());
        dto.setRestriccionesDieteticas(restriccionesDieteticasMapper.toDtoList(entity.getRestriccionesDieteticas()));
        
        return dto;
    }

    public Alimentacion toEntity(AlimentacionDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Alimentacion entity = new Alimentacion();
        
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
        
        // Mapear campos específicos de Alimentacion
        entity.setHoraInicio(dto.getHoraInicio());
        entity.setHoraFinal(dto.getHoraFinal());
        entity.setTipoComida(dto.getTipoComida());
        entity.setMenuIncluido(dto.getMenuIncluido());
        entity.setLatitud(dto.getLatitud());
        entity.setLongitud(dto.getLongitud());
        
        // Nota: restriccionesDieteticas tiene relación bidireccional, se maneja desde el servicio
        if (dto.getRestriccionesDieteticas() != null) {
            entity.setRestriccionesDieteticas(restriccionesDieteticasMapper.toEntityList(dto.getRestriccionesDieteticas()));
            // Establecer la relación inversa
            entity.getRestriccionesDieteticas().forEach(restriccion -> restriccion.setAlimentacion(entity));
        }
        
        return entity;
    }
}