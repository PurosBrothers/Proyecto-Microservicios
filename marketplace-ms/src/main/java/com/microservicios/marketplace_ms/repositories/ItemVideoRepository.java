package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.ItemVideo;

public interface ItemVideoRepository extends JpaRepository<ItemVideo, Long> {
}