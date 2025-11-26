package com.microservicios.gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Gateway Server para el Marketplace Turístico
 * 
 * Funcionalidades:
 * - Punto de entrada único para todos los microservicios
 * - Enrutamiento automático basado en paths (/api/users, /api/payments, etc.)
 * - Load balancing automático con Eureka
 * - Configuración CORS para frontend
 * - Soporte para validación JWT (opcional)
 * 
 * Puerto: 9090
 * Config Server: http://localhost:8888
 * Eureka: http://localhost:9989
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

}
