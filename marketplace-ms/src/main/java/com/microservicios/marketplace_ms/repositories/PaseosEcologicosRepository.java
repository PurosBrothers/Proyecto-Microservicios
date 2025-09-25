package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.PaseosEcologicos;

public interface PaseosEcologicosRepository extends JpaRepository<PaseosEcologicos, Long> {
}