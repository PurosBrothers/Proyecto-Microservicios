package com.microservicios.marketplace_ms.mappers;

import com.microservicios.marketplace_ms.dtos.AlojamientoDTO;
import com.microservicios.marketplace_ms.dtos.MapsDTO;
import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.entities.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlojamientoMapper {

    @Autowired
    private RequisitosEspecialesMapper requisitosEspecialesMapper;

    public AlojamientoDTO toDto(Alojamiento entity) {
        if (entity == null) {
            return null;
        }

        AlojamientoDTO dto = new AlojamientoDTO();

        // Mapear campos de la clase padre
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo());
        dto.setLugarInicio(entity.getLugarInicio());
        dto.setPrecio(entity.getPrecio());
        dto.setFechaDisponibilidadInicio(entity.getFechaDisponibilidadInicio());
        dto.setFechaDisponibilidadFin(entity.getFechaDisponibilidadFin());
        dto.setCapacidadMaxima(entity.getCapacidadMaxima());
        dto.setUsuarioId(entity.getUsuarioId());
        dto.setPaisDestino(entity.getPaisDestino());
        dto.setFlag(entity.getFlag());
        dto.setPopulation(entity.getPopulation());
        dto.setGini(entity.getGini());
        dto.setFifa(entity.getFifa());
        if (entity.getMaps() != null) {
            MapsDTO mapsDTO = new MapsDTO();
            mapsDTO.setGoogleMaps(entity.getMaps().getGoogleMaps());
            mapsDTO.setOpenStreetMaps(entity.getMaps().getOpenStreetMaps());
            dto.setMaps(mapsDTO);
        }
        dto.setRequisitosEspeciales(requisitosEspecialesMapper.toDtoList(entity.getRequisitosEspeciales()));

        // Mapear campos específicos de Alojamiento
        dto.setFechaCheckin(entity.getFechaCheckin());
        dto.setFechaCheckout(entity.getFechaCheckout());
        dto.setTipoInmueble(entity.getTipoInmueble());
        dto.setNumeroBanos(entity.getNumeroBanos());
        dto.setNumeroHabitaciones(entity.getNumeroHabitaciones());
        dto.setLat(entity.getLat());
        dto.setLng(entity.getLng());

        return dto;
    }

    public Alojamiento toEntity(AlojamientoDTO dto) {
        if (dto == null) {
            return null;
        }

        Alojamiento entity = new Alojamiento();

        // Mapear campos de la clase padre
        entity.setId(dto.getId());
        entity.setTipo(dto.getTipo());
        entity.setLugarInicio(dto.getLugarInicio());
        entity.setPrecio(dto.getPrecio());
        entity.setFechaDisponibilidadInicio(dto.getFechaDisponibilidadInicio());
        entity.setFechaDisponibilidadFin(dto.getFechaDisponibilidadFin());
        entity.setCapacidadMaxima(dto.getCapacidadMaxima());
        entity.setUsuarioId(dto.getUsuarioId());
        entity.setPaisDestino(dto.getPaisDestino());
        entity.setFlag(dto.getFlag());
        entity.setPopulation(dto.getPopulation());
        entity.setGini(dto.getGini());
        entity.setFifa(dto.getFifa());
        if (dto.getMaps() != null) {
            Maps maps = new Maps();
            maps.setGoogleMaps(dto.getMaps().getGoogleMaps());
            maps.setOpenStreetMaps(dto.getMaps().getOpenStreetMaps());
            entity.setMaps(maps);
        }
        entity.setRequisitosEspeciales(requisitosEspecialesMapper.toEntityList(dto.getRequisitosEspeciales()));

        // Mapear campos específicos de Alojamiento
        entity.setFechaCheckin(dto.getFechaCheckin());
        entity.setFechaCheckout(dto.getFechaCheckout());
        entity.setTipoInmueble(dto.getTipoInmueble());
        entity.setNumeroBanos(dto.getNumeroBanos());
        entity.setNumeroHabitaciones(dto.getNumeroHabitaciones());
        entity.setLat(dto.getLat());
        entity.setLng(dto.getLng());

        return entity;
    }
}