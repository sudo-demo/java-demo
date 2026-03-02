package com.cobazaar.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * 请求头处理过滤器
 * 实现配置化的请求头清洗和标准化处理
 * 
 * @author cobazaar
 */
@Slf4j
@Component
public class HeaderProcessorFilter implements GlobalFilter, Ordered {

    /**
     * 需要移除的请求头列表（可配置）
     */
    @Value("${gateway.header.remove.headers:from,X-Real-IP,X-Forwarded-For,X-Forwarded-Host}")
    private List<String> removeHeaders;

    /**
     * 需要标准化的请求头映射（可配置）
     */
    @Value("#{${gateway.header.standardize.map:{'X-Forwarded-Proto':'https','X-Forwarded-Port':'443'}}}")
    private Map<String, String> standardizeHeaders;

    /**
     * 需要添加的默认请求头（可配置）
     */
    @Value("#{${gateway.header.default.map:{'X-Gateway':'true','X-Source':'gateway'}}}")
    private Map<String, String> defaultHeaders;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder requestBuilder = request.mutate();

        // 清洗需要移除的请求头
        if (!CollectionUtils.isEmpty(removeHeaders)) {
            removeHeaders.forEach(header -> {
                if (request.getHeaders().containsKey(header)) {
                    requestBuilder.headers(httpHeaders -> httpHeaders.remove(header));
                    log.debug("移除请求头: {}", header);
                }
            });
        }

        // 标准化请求头
        if (!CollectionUtils.isEmpty(standardizeHeaders)) {
            standardizeHeaders.forEach((header, value) -> {
                requestBuilder.header(header, value);
                log.debug("标准化请求头: {} = {}", header, value);
            });
        }

        // 添加默认请求头
        if (!CollectionUtils.isEmpty(defaultHeaders)) {
            defaultHeaders.forEach((header, value) -> {
                if (!request.getHeaders().containsKey(header)) {
                    requestBuilder.header(header, value);
                    log.debug("添加默认请求头: {} = {}", header, value);
                }
            });
        }

        // 构建新的请求
        ServerHttpRequest newRequest = requestBuilder.build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        log.debug("请求头处理完成，路径: {}", request.getURI().getPath());
        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100; // 在认证过滤器之前执行
    }
}