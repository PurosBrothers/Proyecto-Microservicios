package com.microservicios.payment_ms.resolvers;

import com.microservicios.payment_ms.models.EstadoPago;
import com.microservicios.payment_ms.models.Pago;
import com.microservicios.payment_ms.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PagoResolver {

    @Autowired
    private PagoService pagoService;

    @QueryMapping
    public List<Pago> pagos() {
        return pagoService.findAll();
    }

    @QueryMapping
    public Pago pago(@Argument Long id) {
        Optional<Pago> pago = pagoService.findById(id);
        return pago.orElse(null);
    }

    @QueryMapping
    public Pago pagoByUid(@Argument String uid) {
        Optional<Pago> pago = pagoService.findByUid(uid);
        return pago.orElse(null);
    }

    @QueryMapping
    public List<Pago> pagosByReserva(@Argument Long reservaId) {
        return pagoService.findByReservaId(reservaId);
    }

    @MutationMapping
    public Pago createPago(@Argument String uid, @Argument Long reservaId, @Argument BigDecimal monto,
            @Argument String fechaPago, @Argument EstadoPago estadoPago, @Argument String referencia) {
        ZonedDateTime fecha = ZonedDateTime.parse(fechaPago);
        return pagoService.createPago(uid, reservaId, monto, fecha, estadoPago, referencia);
    }

    @MutationMapping
    public Pago updateEstadoPago(@Argument Long id, @Argument EstadoPago estadoPago) {
        Optional<Pago> pago = pagoService.updateEstadoPago(id, estadoPago);
        return pago.orElse(null);
    }

    @MutationMapping
    public Boolean deletePago(@Argument Long id) {
        return pagoService.deleteById(id);
    }
}