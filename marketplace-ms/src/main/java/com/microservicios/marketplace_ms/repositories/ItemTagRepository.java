package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.ItemTag;

public interface ItemTagRepository extends JpaRepository<ItemTag, Long> {
}