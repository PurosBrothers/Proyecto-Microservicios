package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.Calificacion;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
}