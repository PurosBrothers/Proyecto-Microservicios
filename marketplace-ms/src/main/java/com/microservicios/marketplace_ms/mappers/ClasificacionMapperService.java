package com.microservicios.marketplace_ms.mappers;

import com.microservicios.marketplace_ms.dtos.ClasificacionDTO;
import com.microservicios.marketplace_ms.entities.Clasificacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio utilitario para operaciones de mapeo complejas de Clasificacion
 */
@Service
public class ClasificacionMapperService {

    @Autowired
    private ClasificacionMapper clasificacionMapper;
    
    @Autowired
    private ClasificacionFactory clasificacionFactory;

    /**
     * Mapea una entidad Clasificacion a DTO de forma segura
     */
    public Optional<ClasificacionDTO> toDto(Clasificacion entity) {
        try {
            return Optional.ofNullable(clasificacionMapper.toDto(entity));
        } catch (Exception e) {
            // Log del error si es necesario
            return Optional.empty();
        }
    }

    /**
     * Mapea un DTO a entidad de forma segura
     */
    public Optional<Clasificacion> toEntity(ClasificacionDTO dto) {
        try {
            return Optional.ofNullable(clasificacionMapper.toEntity(dto));
        } catch (Exception e) {
            // Log del error si es necesario
            return Optional.empty();
        }
    }

    /**
     * Mapea una lista de entidades a DTOs de forma segura
     */
    public List<ClasificacionDTO> toDtoList(List<Clasificacion> entities) {
        try {
            return clasificacionMapper.toDtoList(entities);
        } catch (Exception e) {
            // Log del error si es necesario
            return List.of();
        }
    }

    /**
     * Mapea una lista de DTOs a entidades de forma segura
     */
    public List<Clasificacion> toEntityList(List<ClasificacionDTO> dtos) {
        try {
            return clasificacionMapper.toEntityList(dtos);
        } catch (Exception e) {
            // Log del error si es necesario
            return List.of();
        }
    }

    /**
     * Crea una nueva instancia de DTO basada en el tipo
     */
    public Optional<ClasificacionDTO> createDtoByType(String tipo) {
        try {
            return Optional.of(clasificacionFactory.createDtoByType(tipo));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Crea una nueva instancia de entidad basada en el tipo
     */
    public Optional<Clasificacion> createEntityByType(String tipo) {
        try {
            return Optional.of(clasificacionFactory.createEntityByType(tipo));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Actualiza una entidad existente con datos del DTO
     */
    public Clasificacion updateEntityFromDto(Clasificacion existingEntity, ClasificacionDTO dto) {
        if (existingEntity == null || dto == null) {
            throw new IllegalArgumentException("La entidad existente y el DTO no pueden ser null");
        }

        // Verificar que los tipos coincidan
        String entityType = existingEntity.getClass().getSimpleName().toLowerCase();
        String dtoType = dto.getClass().getSimpleName().toLowerCase().replace("dto", "");
        
        if (!entityType.equals(dtoType)) {
            throw new IllegalArgumentException("Los tipos de entidad y DTO no coinciden");
        }

        // Mapear el DTO a una nueva entidad y copiar los valores relevantes
        Clasificacion mappedEntity = clasificacionMapper.toEntity(dto);
        
        // Preservar el ID de la entidad existente
        mappedEntity.setId(existingEntity.getId());
        
        return mappedEntity;
    }
}