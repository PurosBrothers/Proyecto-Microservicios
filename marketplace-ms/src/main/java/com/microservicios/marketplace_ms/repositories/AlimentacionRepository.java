package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.Alimentacion;

public interface AlimentacionRepository extends JpaRepository<Alimentacion, Long> {
}