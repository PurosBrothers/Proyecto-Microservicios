package com.microservicios.transaction_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservicios.transaction_ms.models.ItemCarrito;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
}