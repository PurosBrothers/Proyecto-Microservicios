package com.microservicios.payment_ms.resolvers;

import com.microservicios.payment_ms.models.ClienteBanco;
import com.microservicios.payment_ms.services.ClienteBancoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ClienteBancoResolver {

    @Autowired
    private ClienteBancoService clienteBancoService;

    @QueryMapping
    public List<ClienteBanco> clienteBancos() {
        return clienteBancoService.findAll();
    }

    @QueryMapping
    public ClienteBanco clienteBanco(@Argument Long id) {
        Optional<ClienteBanco> cliente = clienteBancoService.findById(id);
        return cliente.orElse(null);
    }

    @QueryMapping
    public ClienteBanco clienteBancoByUid(@Argument String uid) {
        Optional<ClienteBanco> cliente = clienteBancoService.findByUid(uid);
        return cliente.orElse(null);
    }

    @MutationMapping
    public ClienteBanco createClienteBanco(@Argument String uid, @Argument String cuentaBancariaEncrypted,
            @Argument String claveBancariaEncrypted) {
        return clienteBancoService.createClienteBanco(uid, cuentaBancariaEncrypted, claveBancariaEncrypted);
    }

    @MutationMapping
    public ClienteBanco updateCuentaBancaria(@Argument Long id, @Argument String cuentaBancariaEncrypted) {
        Optional<ClienteBanco> cliente = clienteBancoService.updateCuentaBancaria(id, cuentaBancariaEncrypted);
        return cliente.orElse(null);
    }

    @MutationMapping
    public Boolean deleteClienteBanco(@Argument Long id) {
        return clienteBancoService.deleteById(id);
    }
}