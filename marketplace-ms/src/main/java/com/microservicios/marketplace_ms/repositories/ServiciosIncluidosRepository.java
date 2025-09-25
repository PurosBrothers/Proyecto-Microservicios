package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.ServiciosIncluidos;

public interface ServiciosIncluidosRepository extends JpaRepository<ServiciosIncluidos, Long> {
}