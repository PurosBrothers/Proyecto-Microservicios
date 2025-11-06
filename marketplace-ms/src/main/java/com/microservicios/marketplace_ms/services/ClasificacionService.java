package com.microservicios.marketplace_ms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservicios.marketplace_ms.entities.Alimentacion;
import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.entities.PaseosEcologicos;
import com.microservicios.marketplace_ms.entities.Transporte;
import com.microservicios.marketplace_ms.exceptions.InvalidProviderException;
import com.microservicios.marketplace_ms.repositories.ClasificacionRepository;
import com.microservicios.marketplace_ms.security.JwtSecurityContext;

@Service
public class ClasificacionService {

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @Autowired
    private JwtSecurityContext jwtSecurityContext;

    public Clasificacion createClasificacion(Clasificacion clasificacion) {
        // Establecer el usuarioId desde el JWT del usuario autenticado
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            throw new InvalidProviderException("Usuario no autenticado");
        }
        
        // La validación de PROVEEDOR ya se hace a nivel de Spring Security
        clasificacion.setUsuarioId(currentUserId);
        
        return clasificacionRepository.save(clasificacion);
    }

    public Optional<Clasificacion> getClasificacionById(Long id) {
        return clasificacionRepository.findById(id);
    }

    public List<Clasificacion> getAllClasificaciones() {
        return clasificacionRepository.findAll();
    }

    public Clasificacion updateClasificacion(Long id, Clasificacion clasificacion) {
        Optional<Clasificacion> existingOpt = clasificacionRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Clasificacion not found");
        }
        
        Clasificacion existing = existingOpt.get();
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        
        // Verificar que el usuario actual es el propietario de la clasificación
        if (!existing.getUsuarioId().equals(currentUserId)) {
            throw new InvalidProviderException("Solo puedes actualizar tus propias clasificaciones");
        }
        
        // Mantener el usuarioId original (no permitir cambio de propietario)
        clasificacion.setId(id);
        clasificacion.setUsuarioId(existing.getUsuarioId());
        
        return clasificacionRepository.save(clasificacion);
    }

    public void deleteClasificacion(Long id) {
        Optional<Clasificacion> existingOpt = clasificacionRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Clasificacion not found");
        }
        
        Clasificacion existing = existingOpt.get();
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        
        // Verificar que el usuario actual es el propietario de la clasificación
        if (!existing.getUsuarioId().equals(currentUserId)) {
            throw new InvalidProviderException("Solo puedes eliminar tus propias clasificaciones");
        }
        
        clasificacionRepository.deleteById(id);
    }

    // Métodos específicos para Alojamiento
    public Optional<Alojamiento> getAlojamientoById(Long id) {
        return clasificacionRepository.findById(id)
                .filter(c -> c instanceof Alojamiento)
                .map(c -> (Alojamiento) c);
    }

    public List<Alojamiento> getAllAlojamientos() {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Alojamiento)
                .map(c -> (Alojamiento) c)
                .toList();
    }

    // Métodos específicos para Alimentacion
    public Optional<Alimentacion> getAlimentacionById(Long id) {
        return clasificacionRepository.findById(id)
                .filter(c -> c instanceof Alimentacion)
                .map(c -> (Alimentacion) c);
    }

    public List<Alimentacion> getAllAlimentaciones() {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Alimentacion)
                .map(c -> (Alimentacion) c)
                .toList();
    }

    // Métodos específicos para Transporte
    public Optional<Transporte> getTransporteById(Long id) {
        return clasificacionRepository.findById(id)
                .filter(c -> c instanceof Transporte)
                .map(c -> (Transporte) c);
    }

    public List<Transporte> getAllTransportes() {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Transporte)
                .map(c -> (Transporte) c)
                .toList();
    }

    // Métodos específicos para PaseosEcologicos
    public Optional<PaseosEcologicos> getPaseosEcologicosById(Long id) {
        return clasificacionRepository.findById(id)
                .filter(c -> c instanceof PaseosEcologicos)
                .map(c -> (PaseosEcologicos) c);
    }

    public List<PaseosEcologicos> getAllPaseosEcologicos() {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof PaseosEcologicos)
                .map(c -> (PaseosEcologicos) c)
                .toList();
    }

    // Métodos para buscar por usuario
    public List<Clasificacion> getClasificacionesByUsuario(String usuarioId) {
        return clasificacionRepository.findAll().stream()
                .filter(c -> usuarioId.equals(c.getUsuarioId()))
                .toList();
    }

    public List<Alojamiento> getAlojamientosByUsuario(String usuarioId) {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Alojamiento && usuarioId.equals(c.getUsuarioId()))
                .map(c -> (Alojamiento) c)
                .toList();
    }

    public List<Alimentacion> getAlimentacionesByUsuario(String usuarioId) {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Alimentacion && usuarioId.equals(c.getUsuarioId()))
                .map(c -> (Alimentacion) c)
                .toList();
    }

    public List<Transporte> getTransportesByUsuario(String usuarioId) {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Transporte && usuarioId.equals(c.getUsuarioId()))
                .map(c -> (Transporte) c)
                .toList();
    }

    public List<PaseosEcologicos> getPaseosEcologicosByUsuario(String usuarioId) {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof PaseosEcologicos && usuarioId.equals(c.getUsuarioId()))
                .map(c -> (PaseosEcologicos) c)
                .toList();
    }

    // Métodos para obtener clasificaciones del usuario actual (desde JWT)
    public List<Clasificacion> getMyClasificaciones() {
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return getClasificacionesByUsuario(currentUserId);
    }

    public List<Alojamiento> getMyAlojamientos() {
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return getAlojamientosByUsuario(currentUserId);
    }

    public List<Alimentacion> getMyAlimentaciones() {
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return getAlimentacionesByUsuario(currentUserId);
    }

    public List<Transporte> getMyTransportes() {
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return getTransportesByUsuario(currentUserId);
    }

    public List<PaseosEcologicos> getMyPaseosEcologicos() {
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return getPaseosEcologicosByUsuario(currentUserId);
    }
}