package com.microservicios.payment_ms.init;

import com.microservicios.payment_ms.models.EstadoPago;
import com.microservicios.payment_ms.services.ClienteBancoService;
import com.microservicios.payment_ms.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Component
public class DbInitializer implements CommandLineRunner {

        @Autowired
        private ClienteBancoService clienteBancoService;

        @Autowired
        private PagoService pagoService;

        @Override
        public void run(String... args) throws Exception {
                // Inicializar datos de ejemplo para pagos

                // Crear un cliente de banco
                clienteBancoService.createClienteBanco("2a36ca84-0166-46b2-80b4-0838987388bf", "encrypted_account_123",
                                "encrypted_key_123",
                                new BigDecimal("3000.00"));

                // Crear algunos pagos
                pagoService.createPago("pago1", 1L, new BigDecimal("100.00"), ZonedDateTime.now(),
                                EstadoPago.COMPLETADO,
                                "Ref001");
                pagoService.createPago("pago2", 2L, new BigDecimal("200.00"), ZonedDateTime.now(), EstadoPago.PENDIENTE,
                                "Ref002");

                System.out.println("Datos iniciales de pagos cargados");
        }
}