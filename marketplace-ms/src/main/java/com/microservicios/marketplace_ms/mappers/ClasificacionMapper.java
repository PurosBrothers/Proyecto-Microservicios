package com.microservicios.marketplace_ms.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.microservicios.marketplace_ms.dtos.*;
import com.microservicios.marketplace_ms.entities.*;

@Service
public class ClasificacionMapper {
    
    public ClasificacionDTO toDTO(Clasificacion clasificacion) {
        if (clasificacion == null) {
            return null;
        }
        
        ClasificacionDTO dto = null;
        
        if (clasificacion instanceof Alojamiento) {
            dto = toAlojamientoDTO((Alojamiento) clasificacion);
        } else if (clasificacion instanceof Alimentacion) {
            dto = toAlimentacionDTO((Alimentacion) clasificacion);
        } else if (clasificacion instanceof Transporte) {
            dto = toTransporteDTO((Transporte) clasificacion);
        } else if (clasificacion instanceof PaseosEcologicos) {
            dto = toPaseosEcologicosDTO((PaseosEcologicos) clasificacion);
        }
        
        return dto;
    }
    
    public AlojamientoDTO toAlojamientoDTO(Alojamiento alojamiento) {
        if (alojamiento == null) {
            return null;
        }
        
        AlojamientoDTO dto = new AlojamientoDTO();
        populateCommonFields(dto, alojamiento);
        
        dto.setFechaCheckin(alojamiento.getFechaCheckin());
        dto.setFechaCheckout(alojamiento.getFechaCheckout());
        dto.setTipoInmueble(alojamiento.getTipoInmueble());
        dto.setNumeroBanos(alojamiento.getNumeroBanos());
        dto.setNumeroHabitaciones(alojamiento.getNumeroHabitaciones());
        dto.setLat(alojamiento.getLat() != null ? java.util.Optional.of(alojamiento.getLat()) : java.util.Optional.empty());
        dto.setLng(alojamiento.getLng() != null ? java.util.Optional.of(alojamiento.getLng()) : java.util.Optional.empty());
        dto.setDireccion(alojamiento.getDireccion());
        dto.setTemperaturaActual(alojamiento.getTemperaturaActual());
        dto.setViento(alojamiento.getViento());
        dto.setCodigoClima(alojamiento.getCodigoClima());
        dto.setLluvia(alojamiento.getLluvia());
        dto.setPrecipitacion(alojamiento.getPrecipitacion());
        dto.setProbabilidadPrecipitacion(alojamiento.getProbabilidadPrecipitacion());
        
        return dto;
    }
    
    public AlimentacionDTO toAlimentacionDTO(Alimentacion alimentacion) {
        if (alimentacion == null) {
            return null;
        }
        
        AlimentacionDTO dto = new AlimentacionDTO();
        populateCommonFields(dto, alimentacion);
        
        dto.setHoraInicio(alimentacion.getHoraInicio());
        dto.setHoraFinal(alimentacion.getHoraFinal());
        dto.setTipoComida(alimentacion.getTipoComida());
        dto.setMenuIncluido(alimentacion.getMenuIncluido());
        dto.setLatitud(alimentacion.getLatitud());
        dto.setLongitud(alimentacion.getLongitud());
        dto.setRestriccionesDieteticas(toRestriccionesDieteticasDTOList(alimentacion.getRestriccionesDieteticas()));
        
        return dto;
    }
    
    public TransporteDTO toTransporteDTO(Transporte transporte) {
        if (transporte == null) {
            return null;
        }
        
        TransporteDTO dto = new TransporteDTO();
        populateCommonFields(dto, transporte);
        
        dto.setLugarDestino(transporte.getLugarDestino());
        dto.setHoraSalida(transporte.getHoraSalida());
        dto.setHoraLlegada(transporte.getHoraLlegada());
        dto.setTipoTransporte(transporte.getTipoTransporte());
        dto.setDuracionViaje(transporte.getDuracionViaje());
        dto.setRutaGps(transporte.getRutaGps());
        
        return dto;
    }
    
    public PaseosEcologicosDTO toPaseosEcologicosDTO(PaseosEcologicos paseosEcologicos) {
        if (paseosEcologicos == null) {
            return null;
        }
        
        PaseosEcologicosDTO dto = new PaseosEcologicosDTO();
        populateCommonFields(dto, paseosEcologicos);
        
        dto.setDuracionHoras(paseosEcologicos.getDuracionHoras());
        dto.setNivelDificultad(paseosEcologicos.getNivelDificultad());
        dto.setEquipoIncluido(paseosEcologicos.getEquipoIncluido());
        dto.setGuiaIncluido(paseosEcologicos.getGuiaIncluido());
        dto.setEdadMinima(paseosEcologicos.getEdadMinima());
        dto.setPuntoEncuentro(paseosEcologicos.getPuntoEncuentro());
        dto.setRutaEncuentro(paseosEcologicos.getRutaEncuentro());
        
        return dto;
    }
    
    private void populateCommonFields(ClasificacionDTO dto, Clasificacion entity) {
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
        dto.setMaps(toMapsDTO(entity.getMaps()));
        dto.setRequisitosEspeciales(toRequisitosEspecialesDTOList(entity.getRequisitosEspeciales()));
    }
    
    private MapsDTO toMapsDTO(Maps maps) {
        if (maps == null) {
            return null;
        }
        return new MapsDTO(maps.getGoogleMaps(), maps.getOpenStreetMaps());
    }
    
    private List<RequisitosEspecialesDTO> toRequisitosEspecialesDTOList(List<RequisitosEspeciales> requisitosEspeciales) {
        if (requisitosEspeciales == null) {
            return null;
        }
        return requisitosEspeciales.stream()
                .map(req -> new RequisitosEspecialesDTO(req.getId(), req.getRequisito()))
                .collect(Collectors.toList());
    }
    
    public List<ClasificacionDTO> toDTOList(List<Clasificacion> clasificaciones) {
        if (clasificaciones == null) {
            return null;
        }
        return clasificaciones.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private RestriccionesDieteticasDTO toRestriccionesDieteticasDTO(RestriccionesDieteticas restriccionesDieteticas) {
        if (restriccionesDieteticas == null) {
            return null;
        }
        return new RestriccionesDieteticasDTO(restriccionesDieteticas.getId(), restriccionesDieteticas.getNombre());
    }
    
    private List<RestriccionesDieteticasDTO> toRestriccionesDieteticasDTOList(List<RestriccionesDieteticas> restriccionesDieteticas) {
        if (restriccionesDieteticas == null) {
            return null;
        }
        return restriccionesDieteticas.stream()
                .map(this::toRestriccionesDieteticasDTO)
                .collect(Collectors.toList());
    }
}
