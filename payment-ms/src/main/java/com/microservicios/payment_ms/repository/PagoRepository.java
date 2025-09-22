package com.microservicios.payment_ms.repository;

import com.microservicios.payment_ms.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByUid(String uid);

    List<Pago> findByReservaId(Long reservaId);

    List<Pago> findByEstadoPago(String estadoPago);
}