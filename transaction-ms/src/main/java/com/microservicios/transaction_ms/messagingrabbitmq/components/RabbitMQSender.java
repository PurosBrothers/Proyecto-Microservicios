package com.microservicios.transaction_ms.messagingrabbitmq.components;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.transaction_ms.dtos.ProcessPaymentMessageDTO;
import com.microservicios.transaction_ms.dtos.UpdateItemMessageDTO;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendProcessPaymentMessage(ProcessPaymentMessageDTO messageDTO) {
        try {
            String message = objectMapper.writeValueAsString(messageDTO);
            System.out.println("Intentando enviar mensaje a RabbitMQ (process-payment)...");
            System.out.println("Host configurado: " + rabbitTemplate.getConnectionFactory().getHost());
            System.out.println("Puerto configurado: " + rabbitTemplate.getConnectionFactory().getPort());
            rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_NAME, "payment.process", message);
            System.out.println("Mensaje enviado a process-payment: " + message);
        } catch (Exception e) {
            System.out.println("Error enviando mensaje a process-payment: " + e.getMessage());
            if (rabbitTemplate.getConnectionFactory() != null) {
                System.err.println("Host de conexión: " + rabbitTemplate.getConnectionFactory().getHost());
                System.err.println("Puerto de conexión: " + rabbitTemplate.getConnectionFactory().getPort());
            }
            e.printStackTrace();
        }
    }

    public void sendUpdateItemMessage(UpdateItemMessageDTO messageDTO) {
        try {
            String message = objectMapper.writeValueAsString(messageDTO);
            System.out.println("Intentando enviar mensaje a RabbitMQ (update-item)...");
            System.out.println("Host configurado: " + rabbitTemplate.getConnectionFactory().getHost());
            System.out.println("Puerto configurado: " + rabbitTemplate.getConnectionFactory().getPort());
            rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_NAME, "marketplace.update-item", message);
            System.out.println("Mensaje enviado a update-item: " + message);
        } catch (Exception e) {
            System.out.println("Error enviando mensaje a update-item: " + e.getMessage());
            if (rabbitTemplate.getConnectionFactory() != null) {
                System.err.println("Host de conexión: " + rabbitTemplate.getConnectionFactory().getHost());
                System.err.println("Puerto de conexión: " + rabbitTemplate.getConnectionFactory().getPort());
            }
            e.printStackTrace();
        }
    }
}