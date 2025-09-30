package com.microservicios.marketplace_ms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservicios.marketplace_ms.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i JOIN FETCH i.clasificacion WHERE TYPE(i.clasificacion) = :type")
    List<Item> findByClasificacionType(@Param("type") Class<?> type);
}