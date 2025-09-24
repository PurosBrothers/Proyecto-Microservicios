package com.microservicios.marketplace_ms;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootTest
class MarketplaceMsApplicationTests {

	@Test
	void contextLoads() {
	}

	static final String topicExchangeName = "spring-boot-exchange";

	static final String queueName = "spring-boot";

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
  	}

}
