package com.microservicios.transaction_ms.messagingrabbitmq.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.transaction_ms.dtos.PaymentConfirmationMessageDTO;
import com.microservicios.transaction_ms.services.TransaccionService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReceiver {

    @Autowired
    private TransaccionService transaccionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PAYMENT_CONFIRMATION)
    public void receivePaymentConfirmation(String message) {
        System.out.println("Confirmación de pago recibida: " + message);
        try {
            PaymentConfirmationMessageDTO dto = objectMapper.readValue(message, PaymentConfirmationMessageDTO.class);
            System.out.println("Estado de pago: " + dto.getEstadoPago());
            if ("COMPLETADO".equals(dto.getEstadoPago())) {
                // Actualizar transacción a COMPLETED y mover items
                transaccionService.completeTransaction(dto.getReservaId());
            } else if ("CANCELADO".equals(dto.getEstadoPago())) {
                // Saldo insuficiente, marcar como FAILED
                System.out.println("Pago cancelado por saldo insuficiente: " + dto.getReferencia());
                transaccionService.updateTransactionStatus(dto.getReservaId(), "FAILED");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}