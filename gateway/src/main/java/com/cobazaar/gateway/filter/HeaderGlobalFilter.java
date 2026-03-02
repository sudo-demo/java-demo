package com.cobazaar.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局请求头过滤器
 * 清洗请求头中的from参数，作用于所有微服务
 */
@Slf4j
@Component
public class HeaderGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // 获取原始请求头
        ServerHttpRequest.Builder builder = request.mutate();
        
        // 清洗from参数
        builder.headers(httpHeaders -> {
            // 移除from参数
            httpHeaders.remove("from");
            log.debug("已清除请求头中的from参数");
        });
        
        // 构建新的请求
        ServerHttpRequest newRequest = builder.build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        
        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        // 设置较高的优先级，确保在其他过滤器之前执行
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}