package com.microservicios.marketplace_ms.mappers;

import com.microservicios.marketplace_ms.dtos.AlimentacionDTO;
import com.microservicios.marketplace_ms.dtos.AlojamientoDTO;
import com.microservicios.marketplace_ms.dtos.ClasificacionDTO;
import com.microservicios.marketplace_ms.dtos.PaseosEcologicosDTO;
import com.microservicios.marketplace_ms.dtos.TransporteDTO;
import com.microservicios.marketplace_ms.entities.Alimentacion;
import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.entities.PaseosEcologicos;
import com.microservicios.marketplace_ms.entities.Transporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClasificacionMapper {

    @Autowired
    private AlojamientoMapper alojamientoMapper;
    
    @Autowired
    private AlimentacionMapper alimentacionMapper;
    
    @Autowired
    private TransporteMapper transporteMapper;
    
    @Autowired
    private PaseosEcologicosMapper paseosEcologicosMapper;

    /**
     * Convierte una entidad Clasificacion a su DTO correspondiente usando polimorfismo
     */
    public ClasificacionDTO toDto(Clasificacion entity) {
        if (entity == null) {
            return null;
        }

        // Usar instanceof para determinar el tipo específico y delegar al mapper apropiado
        if (entity instanceof Alojamiento) {
            return alojamientoMapper.toDto((Alojamiento) entity);
        } else if (entity instanceof Alimentacion) {
            return alimentacionMapper.toDto((Alimentacion) entity);
        } else if (entity instanceof Transporte) {
            return transporteMapper.toDto((Transporte) entity);
        } else if (entity instanceof PaseosEcologicos) {
            return paseosEcologicosMapper.toDto((PaseosEcologicos) entity);
        }

        throw new IllegalArgumentException("Tipo de clasificación no soportado: " + entity.getClass().getSimpleName());
    }

    /**
     * Convierte un DTO ClasificacionDTO a su entidad correspondiente usando polimorfismo
     */
    public Clasificacion toEntity(ClasificacionDTO dto) {
        if (dto == null) {
            return null;
        }

        // Usar instanceof para determinar el tipo específico y delegar al mapper apropiado
        if (dto instanceof AlojamientoDTO) {
            return alojamientoMapper.toEntity((AlojamientoDTO) dto);
        } else if (dto instanceof AlimentacionDTO) {
            return alimentacionMapper.toEntity((AlimentacionDTO) dto);
        } else if (dto instanceof TransporteDTO) {
            return transporteMapper.toEntity((TransporteDTO) dto);
        } else if (dto instanceof PaseosEcologicosDTO) {
            return paseosEcologicosMapper.toEntity((PaseosEcologicosDTO) dto);
        }

        throw new IllegalArgumentException("Tipo de clasificación DTO no soportado: " + dto.getClass().getSimpleName());
    }

    /**
     * Convierte una lista de entidades a DTOs
     */
    public List<ClasificacionDTO> toDtoList(List<Clasificacion> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de DTOs a entidades
     */
    public List<Clasificacion> toEntityList(List<ClasificacionDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
