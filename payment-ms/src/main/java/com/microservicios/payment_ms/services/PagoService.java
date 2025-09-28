package com.microservicios.payment_ms.services;

import com.microservicios.payment_ms.models.ClienteBanco;
import com.microservicios.payment_ms.models.EstadoPago;
import com.microservicios.payment_ms.models.Pago;
import com.microservicios.payment_ms.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> findById(Long id) {
        return pagoRepository.findById(id);
    }

    public Optional<Pago> findByUid(String uid) {
        return pagoRepository.findByUid(uid);
    }

    public List<Pago> findByReservaId(Long reservaId) {
        return pagoRepository.findByReservaId(reservaId);
    }

    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Pago createPago(String uid, Long reservaId, BigDecimal monto, ZonedDateTime fechaPago, EstadoPago estadoPago,
            String referencia) {
        Pago pago = new Pago(uid, reservaId, monto, fechaPago, estadoPago, referencia);
        return pagoRepository.save(pago);
    }

    public Optional<Pago> updateEstadoPago(Long id, EstadoPago nuevoEstado) {
        Optional<Pago> pagoOpt = pagoRepository.findById(id);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.setEstadoPago(nuevoEstado);
            return Optional.of(pagoRepository.save(pago));
        }
        return Optional.empty();
    }

    public boolean deleteById(Long id) {
        if (pagoRepository.existsById(id)) {
            pagoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Autowired
    private ClienteBancoService clienteBancoService;

    // Procesar pago desde mensaje
    public Pago processPayment(String uid, Long reservaId, BigDecimal monto) {
        Optional<ClienteBanco> clienteOpt = clienteBancoService.findByUid(uid);
        if (clienteOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado");
        }
        ClienteBanco cliente = clienteOpt.get();
        if (cliente.getSaldo().compareTo(monto) < 0) {
            // Saldo insuficiente, crear pago CANCELADO
            Pago pago = new Pago();
            pago.setUid(uid);
            pago.setReservaId(reservaId);
            pago.setMonto(monto);
            pago.setFechaPago(ZonedDateTime.now());
            pago.setEstadoPago(EstadoPago.CANCELADO);
            pago.setReferencia("REF-" + reservaId + "-INSUFICIENTE");
            return save(pago);
        }
        // Saldo suficiente, restar y crear pago COMPLETADO
        cliente.setSaldo(cliente.getSaldo().subtract(monto));
        clienteBancoService.save(cliente);
        Pago pago = new Pago();
        pago.setUid(uid);
        pago.setReservaId(reservaId);
        pago.setMonto(monto);
        pago.setFechaPago(ZonedDateTime.now());
        pago.setEstadoPago(EstadoPago.COMPLETADO);
        pago.setReferencia("REF-" + reservaId);
        return save(pago);
    }
}