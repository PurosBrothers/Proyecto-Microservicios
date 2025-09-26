package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.Clasificacion;

public interface ClasificacionRepository extends JpaRepository<Clasificacion, Long> {
}