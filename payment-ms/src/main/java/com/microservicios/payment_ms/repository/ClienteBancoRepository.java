package com.microservicios.payment_ms.repository;

import com.microservicios.payment_ms.models.ClienteBanco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteBancoRepository extends JpaRepository<ClienteBanco, Long> {

    Optional<ClienteBanco> findByUid(String uid);
}