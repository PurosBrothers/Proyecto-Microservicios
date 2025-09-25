package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.Transporte;

public interface TransporteRepository extends JpaRepository<Transporte, Long> {
}