package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.ItemLink;

public interface ItemLinkRepository extends JpaRepository<ItemLink, Long> {
}