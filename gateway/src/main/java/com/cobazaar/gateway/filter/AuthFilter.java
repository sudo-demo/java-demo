package com.cobazaar.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证过滤器
 * 处理基础的认证逻辑，严格按照规范放行Swagger等公共资源路径
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "gateway.auth")
@Data
public class AuthFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    // 需要放行的公共资源路径白名单（从配置文件读取）
    private List<String> whitelistPaths = new ArrayList<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 打印请求信息，用于调试
        log.debug("收到请求: {} {}", request.getMethod(), path);
        
        // 检查是否在白名单中
        if (shouldSkipAuth(path)) {
            log.debug("跳过认证检查: {}", path);
            return chain.filter(exchange);
        }
        
        // 对于业务接口，直接放行，让后端服务处理认证逻辑
        // 这样后端服务可以返回更详细的认证错误信息
        log.debug("业务接口，直接放行: {} {}", request.getMethod(), path);
        return chain.filter(exchange);
    }

    /**
     * 判断是否跳过认证
     * 严格按照项目规范检查所有需要放行的路径
     */
    private boolean shouldSkipAuth(String path) {
        // 打印路径匹配日志，用于调试
        boolean skipAuth = whitelistPaths.stream()
                .anyMatch(pattern -> {
                    boolean matched = pathMatcher.match(pattern, path);
                    if (matched) {
                        log.debug("路径匹配成功: path={}, pattern={}", path, pattern);
                    }
                    return matched;
                });
        
        if (!skipAuth) {
            log.debug("路径未匹配白名单: path={}", path);
        }
        
        return skipAuth;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 200;
    }
}