package com.microservicios.marketplace_ms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservicios.marketplace_ms.entities.Calificacion;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    
    // Encontrar calificaciones por item
    List<Calificacion> findByItemId(Long itemId);
    
    // Encontrar calificaciones por usuario
    List<Calificacion> findByUid(Long uid);
    
    // Verificar si un usuario ya calificó un item
    boolean existsByItemIdAndUid(Long itemId, Long uid);
    
    // Obtener promedio de calificaciones de un item
    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.item.id = :itemId")
    Double findAverageRatingByItemId(@Param("itemId") Long itemId);
    
    // Contar calificaciones por item
    long countByItemId(Long itemId);
}