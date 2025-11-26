package com.microservicios.marketplace_ms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservicios.marketplace_ms.entities.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    
    // Encontrar comentarios padre de un item (sin parent)
    List<Comentario> findByItemIdAndParentIsNull(Long itemId);
    
    // Encontrar respuestas de un comentario padre
    List<Comentario> findByParentId(Long parentId);
    
    // Encontrar comentarios por usuario
    List<Comentario> findByUid(String uid);
    
    // Encontrar comentarios padre con sus respuestas
    @Query("SELECT c FROM Comentario c LEFT JOIN FETCH c.replies WHERE c.item.id = :itemId AND c.parent IS NULL ORDER BY c.id DESC")
    List<Comentario> findComentariosPadreWithRepliesByItemId(@Param("itemId") Long itemId);
    
    // Contar respuestas de un comentario
    long countByParentId(Long parentId);
}