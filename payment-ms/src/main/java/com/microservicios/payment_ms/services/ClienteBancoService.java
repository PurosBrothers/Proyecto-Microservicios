package com.microservicios.payment_ms.services;

import com.microservicios.payment_ms.models.ClienteBanco;
import com.microservicios.payment_ms.repository.ClienteBancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteBancoService {

    @Autowired
    private ClienteBancoRepository clienteBancoRepository;

    public List<ClienteBanco> findAll() {
        return clienteBancoRepository.findAll();
    }

    public Optional<ClienteBanco> findById(Long id) {
        return clienteBancoRepository.findById(id);
    }

    public Optional<ClienteBanco> findByUid(String uid) {
        return clienteBancoRepository.findByUid(uid);
    }

    public ClienteBanco save(ClienteBanco clienteBanco) {
        return clienteBancoRepository.save(clienteBanco);
    }

    public ClienteBanco createClienteBanco(String uid, String cuentaBancariaEncrypted, String claveBancariaEncrypted,
            BigDecimal saldo) {
        String claveHashed = new BCryptPasswordEncoder().encode(claveBancariaEncrypted);
        ClienteBanco clienteBanco = new ClienteBanco(uid, cuentaBancariaEncrypted, claveHashed, saldo);
        return clienteBancoRepository.save(clienteBanco);
    }

    public Optional<ClienteBanco> updateCuentaBancaria(Long id, String nuevaCuentaEncrypted) {
        Optional<ClienteBanco> clienteOpt = clienteBancoRepository.findById(id);
        if (clienteOpt.isPresent()) {
            ClienteBanco cliente = clienteOpt.get();
            cliente.setCuentaBancariaEncrypted(nuevaCuentaEncrypted);
            return Optional.of(clienteBancoRepository.save(cliente));
        }
        return Optional.empty();
    }

    public boolean deleteById(Long id) {
        if (clienteBancoRepository.existsById(id)) {
            clienteBancoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}