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

import org.springframework.stereotype.Component;

/**
 * Factory para crear instancias específicas de Clasificacion basadas en el tipo
 */
@Component
public class ClasificacionFactory {

    /**
     * Crea una instancia de ClasificacionDTO específica basada en el tipo
     */
    public ClasificacionDTO createDtoByType(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no puede ser null");
        }

        switch (tipo.toLowerCase()) {
            case "alojamiento":
                return new AlojamientoDTO();
            case "alimentacion":
                return new AlimentacionDTO();
            case "transporte":
                return new TransporteDTO();
            case "paseos ecologicos":
            case "paseosecologicos":
                return new PaseosEcologicosDTO();
            default:
                throw new IllegalArgumentException("Tipo de clasificación no soportado: " + tipo);
        }
    }

    /**
     * Crea una instancia de Clasificacion específica basada en el tipo
     */
    public Clasificacion createEntityByType(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no puede ser null");
        }

        switch (tipo.toLowerCase()) {
            case "alojamiento":
                return new Alojamiento();
            case "alimentacion":
                return new Alimentacion();
            case "transporte":
                return new Transporte();
            case "paseos ecologicos":
            case "paseosecologicos":
                return new PaseosEcologicos();
            default:
                throw new IllegalArgumentException("Tipo de clasificación no soportado: " + tipo);
        }
    }

    /**
     * Obtiene la clase de entidad correspondiente al tipo
     */
    public Class<? extends Clasificacion> getEntityClassByType(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no puede ser null");
        }

        switch (tipo.toLowerCase()) {
            case "alojamiento":
                return Alojamiento.class;
            case "alimentacion":
                return Alimentacion.class;
            case "transporte":
                return Transporte.class;
            case "paseos ecologicos":
            case "paseosecologicos":
                return PaseosEcologicos.class;
            default:
                throw new IllegalArgumentException("Tipo de clasificación no soportado: " + tipo);
        }
    }

    /**
     * Obtiene la clase de DTO correspondiente al tipo
     */
    public Class<? extends ClasificacionDTO> getDtoClassByType(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no puede ser null");
        }

        switch (tipo.toLowerCase()) {
            case "alojamiento":
                return AlojamientoDTO.class;
            case "alimentacion":
                return AlimentacionDTO.class;
            case "transporte":
                return TransporteDTO.class;
            case "paseos ecologicos":
            case "paseosecologicos":
                return PaseosEcologicosDTO.class;
            default:
                throw new IllegalArgumentException("Tipo de clasificación no soportado: " + tipo);
        }
    }
}