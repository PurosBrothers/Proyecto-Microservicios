package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservicios.marketplace_ms.entities.Clasificacion;

@Repository
public interface ClasificacionRepository extends JpaRepository<Clasificacion, Long> {
}