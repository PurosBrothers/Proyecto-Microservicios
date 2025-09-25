package com.microservicios.marketplace_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicios.marketplace_ms.entities.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}