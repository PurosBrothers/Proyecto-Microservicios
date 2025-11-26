package com.microservicios.transaction_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservicios.transaction_ms.models.ItemTransaccion;

public interface ItemTransaccionRepository extends JpaRepository<ItemTransaccion, Long> {
}