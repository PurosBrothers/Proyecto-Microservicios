package com.microservicios.transaction_ms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservicios.transaction_ms.models.CarritoCompra;

public interface CarritoCompraRepository extends JpaRepository<CarritoCompra, Long> {

    Optional<CarritoCompra> findByUID(String uid);
}