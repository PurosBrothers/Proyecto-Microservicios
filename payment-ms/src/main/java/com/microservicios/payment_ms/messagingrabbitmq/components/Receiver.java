package com.microservicios.payment_ms.messagingrabbitmq.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.payment_ms.dtos.PaymentConfirmationMessageDTO;
import com.microservicios.payment_ms.dtos.ProcessPaymentMessageDTO;
import com.microservicios.payment_ms.models.EstadoPago;
import com.microservicios.payment_ms.models.Pago;
import com.microservicios.payment_ms.services.PagoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Component
public class Receiver {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void receiveMessage(String message) {
        System.out.println("Mensaje recibido en payment-ms: " + message);
        try {
            // Deserializar mensaje
            ProcessPaymentMessageDTO dto = objectMapper.readValue(message, ProcessPaymentMessageDTO.class);

            // Procesar pago via servicio
            Pago pago = pagoService.processPayment(dto.getUid(), dto.getReservaId(), dto.getMontoTotal());

            // Enviar confirmación a transaction-ms
            PaymentConfirmationMessageDTO confirmation = new PaymentConfirmationMessageDTO(
                    dto.getUid(),
                    dto.getReservaId(),
                    pago.getEstadoPago(),
                    pago.getReferencia());
            rabbitMQSender.sendPaymentConfirmationMessage(confirmation);

        } catch (Exception e) {
            System.out.println("Error procesando mensaje en payment-ms: " + e.getMessage());
            e.printStackTrace();
        }
    }
}