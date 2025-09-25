package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.Alojamiento;

public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {
}