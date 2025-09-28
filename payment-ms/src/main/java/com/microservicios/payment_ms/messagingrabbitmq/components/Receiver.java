package com.microservicios.payment_ms.messagingrabbitmq.components;

import org.springframework.stereotype.Component;

@Component
public class Receiver {

    public void receiveMessage(String message) {
        System.out.println("Mensaje recibido en payment-ms: " + message);
    }
}