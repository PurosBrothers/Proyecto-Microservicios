package com.microservicios.marketplace_ms.mappers;

import com.microservicios.marketplace_ms.dtos.TransporteDTO;
import com.microservicios.marketplace_ms.entities.Transporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransporteMapper {

    @Autowired
    private RequisitosEspecialesMapper requisitosEspecialesMapper;

    public TransporteDTO toDto(Transporte entity) {
        if (entity == null) {
            return null;
        }
        
        TransporteDTO dto = new TransporteDTO();
        
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
        
        // Mapear campos específicos de Transporte
        dto.setLugarDestino(entity.getLugarDestino());
        dto.setHoraSalida(entity.getHoraSalida());
        dto.setHoraLlegada(entity.getHoraLlegada());
        dto.setTipoTransporte(entity.getTipoTransporte());
        dto.setDuracionViaje(entity.getDuracionViaje());
        dto.setRutaGps(entity.getRutaGps());
        
        return dto;
    }

    public Transporte toEntity(TransporteDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Transporte entity = new Transporte();
        
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
        
        // Mapear campos específicos de Transporte
        entity.setLugarDestino(dto.getLugarDestino());
        entity.setHoraSalida(dto.getHoraSalida());
        entity.setHoraLlegada(dto.getHoraLlegada());
        entity.setTipoTransporte(dto.getTipoTransporte());
        entity.setDuracionViaje(dto.getDuracionViaje());
        entity.setRutaGps(dto.getRutaGps());
        
        return entity;
    }
}