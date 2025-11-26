package com.microservicios.marketplace_ms.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.host:rabbitmq}")
    private String rabbitmqHost;

    @Value("${rabbitmq.port:5672}")
    private int rabbitmqPort;

    @Value("${rabbitmq.username:guest}")
    private String rabbitmqUsername;

    @Value("${rabbitmq.password:guest}")
    private String rabbitmqPassword;

    public static final String TOPIC_EXCHANGE_NAME = "transaction-exchange";

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

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}