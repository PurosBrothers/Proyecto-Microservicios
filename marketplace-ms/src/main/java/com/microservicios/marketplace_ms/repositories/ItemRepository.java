package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}