package com.microservicios.transaction_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservicios.transaction_ms.models.Transaccion;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    java.util.List<Transaccion> findByUID(String uid);
}