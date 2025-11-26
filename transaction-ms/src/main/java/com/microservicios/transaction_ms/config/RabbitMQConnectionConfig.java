package com.microservicios.transaction_ms.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConnectionConfig {

    @Value("${rabbitmq.host:rabbitmq}")
    private String rabbitmqHost;

    @Value("${rabbitmq.port:5672}")
    private int rabbitmqPort;

    @Value("${rabbitmq.username:guest}")
    private String rabbitmqUsername;

    @Value("${rabbitmq.password:guest}")
    private String rabbitmqPassword;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitmqHost);
        connectionFactory.setPort(rabbitmqPort);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        connectionFactory.setConnectionTimeout(30000);
        return connectionFactory;
    }
}