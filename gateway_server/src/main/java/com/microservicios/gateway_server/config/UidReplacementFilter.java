package com.microservicios.gateway_server.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Filtro global que reemplaza placeholders "uid" en las rutas y en el body JSON
 * con el UID del usuario autenticado
 *
 * Este filtro intercepta todas las peticiones HTTP, obtiene el JWT del contexto
 * de seguridad (ya validado por Spring Security), extrae el subject (UID del
 * usuario),
 * y reemplaza cualquier ocurrencia de "uid" en la path y en el body JSON con el
 * UID real.
 */
@Component
@Slf4j
public class UidReplacementFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        log.info("Gateway Filter: Procesando petición a: {}", path);
        log.info("Gateway Filter: Headers: {}", request.getHeaders());

        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication != null && authentication.isAuthenticated())
                .filter(authentication -> authentication.getPrincipal() instanceof Jwt)
                .map(authentication -> (Jwt) authentication.getPrincipal())
                .flatMap(jwt -> {
                    String uid = jwt.getSubject(); // El subject contiene el UID del usuario
                    log.info("Gateway Filter: UID del usuario: {}", uid);

                    if (uid != null && !uid.isEmpty()) {
                        log.info("Gateway Filter: UID válido: {}", uid);
                        // Verificar si la path contiene "/uid"
                        boolean pathModified = false;
                        ServerHttpRequest modifiedRequest = request;

                        if (path.contains("/uid")) {
                            // Reemplazar "/uid" con el UID real
                            String newPath = path.replace("/uid", "/" + uid);
                            log.info("🔄 Reemplazando UID en ruta: {} -> {}", path, newPath);

                            modifiedRequest = request.mutate().path(newPath).build();
                            pathModified = true;
                        }

                        // Verificar si el body contiene "uid" (solo para JSON)
                        String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
                        log.info("Gateway Filter: Content-Type: {}", contentType);
                        if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                            return modifyRequestBody(exchange, modifiedRequest, uid, pathModified, chain);
                        } else {
                            // No es JSON, solo modificar path si fue necesario
                            if (pathModified) {
                                ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
                                return chain.filter(modifiedExchange);
                            }
                        }
                    }

                    // No hay modificaciones necesarias
                    return chain.filter(exchange);
                })
                .switchIfEmpty(Mono
                        .fromRunnable(
                                () -> log.warn("Gateway Filter: No hay autenticación JWT, continuando sin modificar"))
                        .then(chain.filter(exchange))) // Si no hay autenticación JWT, continuar sin modificar
                .onErrorResume(error -> {
                    log.error("❌ Error en filtro de reemplazo UID: {}", error.getMessage(), error);
                    return chain.filter(exchange);
                });
    }

    private Mono<Void> modifyRequestBody(ServerWebExchange exchange, ServerHttpRequest request,
            String uid, boolean pathModified, GatewayFilterChain chain) {
        log.info("Gateway Filter: Modificando body para UID: {}", uid);
        return DataBufferUtils.join(request.getBody())
                .flatMap(dataBuffer -> {
                    try {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);

                        String bodyString = new String(bytes, StandardCharsets.UTF_8);
                        log.info("Gateway Filter: Body original: {}", bodyString);

                        // Siempre recrear el body para evitar problemas de consumo
                        byte[] bodyBytes;
                        if (StringUtils.hasText(bodyString) && bodyString.contains("\"uid\"")) {
                            // Parsear JSON y reemplazar uid
                            JsonNode jsonNode = objectMapper.readTree(bodyString);
                            String modifiedBody = replaceUidInJson(jsonNode, uid);
                            log.info("Gateway Filter: Body modificado: {}", modifiedBody);
                            log.info("🔄 Reemplazando UID en body JSON");
                            bodyBytes = modifiedBody.getBytes(StandardCharsets.UTF_8);
                        } else {
                            // Usar body original
                            bodyBytes = bodyString.getBytes(StandardCharsets.UTF_8);
                        }

                        Flux<DataBuffer> bodyFlux = Flux.just(
                                exchange.getResponse().bufferFactory().wrap(bodyBytes));

                        // Crear request decorator con el body
                        ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(request) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return bodyFlux;
                            }

                            @Override
                            public HttpHeaders getHeaders() {
                                HttpHeaders headers = new HttpHeaders();
                                headers.putAll(super.getHeaders());
                                headers.setContentLength(bodyBytes.length);
                                return headers;
                            }
                        };

                        ServerWebExchange modifiedExchange = exchange.mutate().request(decorator).build();
                        return chain.filter(modifiedExchange);

                    } catch (IOException e) {
                        log.error("❌ Error procesando body JSON: {}", e.getMessage(), e);
                    }

                    return chain.filter(exchange);
                });
    }

    private String replaceUidInJson(JsonNode jsonNode, String uid) {
        try {
            // Crear una copia modificable del JSON
            JsonNode modifiedNode = replaceUidInJsonNode(jsonNode, uid);
            return objectMapper.writeValueAsString(modifiedNode);
        } catch (Exception e) {
            log.warn("❌ Error reemplazando UID en JSON: {}", e.getMessage());
            return jsonNode.toString();
        }
    }

    private JsonNode replaceUidInJsonNode(JsonNode node, String uid) {
        if (node.isObject()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode newNode = mapper.createObjectNode();

            node.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode value = entry.getValue();

                if ("uid".equals(key) && value.isTextual()) {
                    ((com.fasterxml.jackson.databind.node.ObjectNode) newNode).put(key, uid);
                } else {
                    ((com.fasterxml.jackson.databind.node.ObjectNode) newNode).set(key,
                            replaceUidInJsonNode(value, uid));
                }
            });

            return newNode;
        } else if (node.isArray()) {
            com.fasterxml.jackson.databind.node.ArrayNode arrayNode = objectMapper.createArrayNode();
            node.elements().forEachRemaining(element -> arrayNode.add(replaceUidInJsonNode(element, uid)));
            return arrayNode;
        } else {
            return node;
        }
    }

    @Override
    public int getOrder() {
        // Ejecutar después de la autenticación (Ordered.LOWEST_PRECEDENCE ejecuta
        // último)
        return Ordered.LOWEST_PRECEDENCE;
    }
}