package com.microservicios.marketplace_ms.mappers;

import com.microservicios.marketplace_ms.dtos.ClasificacionDTO;
import com.microservicios.marketplace_ms.entities.Clasificacion;

public class ClasificacionMapper {

    public static Clasificacion dtoToEntity(ClasificacionDTO clasificacionDTO) {
        if (clasificacionDTO == null) {
            return null;
        }
        Clasificacion clasificacion = new Clasificacion();
        clasificacion.setId(clasificacionDTO.getId());
        clasificacion.setLugarInicio(clasificacionDTO.getLugarInicio());
        clasificacion.setPrecio(clasificacionDTO.getPrecio());
        clasificacion.setFechaDisponibilidadInicio(clasificacionDTO.getFechaDisponibilidadInicio());
        clasificacion.setFechaDisponibilidadFin(clasificacionDTO.getFechaDisponibilidadFin());
        clasificacion.setCapacidadMaxima(clasificacionDTO.getCapacidadMaxima());
        return clasificacion;
    }

    public static ClasificacionDTO entityToDto(Clasificacion clasificacion) {
        if (clasificacion == null) {
            return null;
        }
        ClasificacionDTO clasificacionDTO = new ClasificacionDTO();
        clasificacionDTO.setId(clasificacion.getId());
        clasificacionDTO.setLugarInicio(clasificacion.getLugarInicio());
        clasificacionDTO.setPrecio(clasificacion.getPrecio());
        clasificacionDTO.setFechaDisponibilidadInicio(clasificacion.getFechaDisponibilidadInicio());
        clasificacionDTO.setFechaDisponibilidadFin(clasificacion.getFechaDisponibilidadFin());
        clasificacionDTO.setCapacidadMaxima(clasificacion.getCapacidadMaxima());
        return clasificacionDTO;
    }
}
