package com.cobazaar.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.fastjson.JSON;
import com.cobazaar.gateway.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Sentinel限流配置类
 * 提供基础的限流处理功能
 * 
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-06
 */
@Slf4j
@Configuration
public class SentinelConfig {

    @Value("${spring.application.name:gateway}")
    private String applicationName;

    /**
     * 初始化Sentinel配置
     */
    @PostConstruct
    public void initSentinelConfig() {
        initBlockHandler();
        log.info("Sentinel configuration initialized for application: {}", applicationName);
    }

    /**
     * 初始化限流处理器
     */
    private void initBlockHandler() {
        BlockRequestHandler blockHandler = new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 429);
                result.put("message", "请求过于频繁，请稍后重试");
                result.put("timestamp", System.currentTimeMillis());
                result.put("path", serverWebExchange.getRequest().getPath().value());
                
                String jsonResponse = JSON.toJSONString(Result.error(429, "请求过于频繁，请稍后重试"));
                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(jsonResponse), String.class);
            }
        };

        GatewayCallbackManager.setBlockHandler(blockHandler);
        log.info("Sentinel block handler initialized");
    }

    /**
     * Sentinel全局过滤器
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGlobalFilter() {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().value();
            String method = exchange.getRequest().getMethodValue();
            
            log.debug("Sentinel monitoring - Path: {}, Method: {}", path, method);
            
            return chain.filter(exchange);
        };
    }
}