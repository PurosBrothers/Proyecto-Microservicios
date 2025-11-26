package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.ItemFoto;

public interface ItemFotoRepository extends JpaRepository<ItemFoto, Long> {
}