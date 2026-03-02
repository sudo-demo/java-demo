package com.cobazaar.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * 路由限流配置类
 * 支持基于Redis的分布式限流
 */
@Configuration
public class RateLimiterConfig {

    /**
     * 用户限流Key解析器
     * 基于用户ID进行限流
     */
    @Bean
    @Primary
    public KeyResolver userKeyResolver() {
        return exchange -> {
            // 从请求头或参数中获取用户标识
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
            if (userId == null) {
                userId = exchange.getRequest().getQueryParams().getFirst("userId");
            }
            // 如果没有用户标识，则使用IP地址
            if (userId == null) {
                userId = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            }
            return Mono.just(userId);
        };
    }

    /**
     * IP限流Key解析器
     * 基于客户端IP进行限流
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String hostAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            return Mono.just(hostAddress);
        };
    }

    /**
     * 路径限流Key解析器
     * 基于请求路径进行限流
     */
    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    /**
     * 默认Redis限流器配置
     * 每秒允许10个请求，突发容量20个
     */
    @Bean
    @Primary
    public RedisRateLimiter defaultRateLimiter() {
        return new RedisRateLimiter(10, 20); // replenishRate, burstCapacity
    }

    /**
     * 严格限流器配置
     * 每秒允许5个请求，突发容量10个
     */
    @Bean
    public RedisRateLimiter strictRateLimiter() {
        return new RedisRateLimiter(5, 10);
    }

    /**
     * 宽松限流器配置
     * 每秒允许50个请求，突发容量100个
     */
    @Bean
    public RedisRateLimiter looseRateLimiter() {
        return new RedisRateLimiter(50, 100);
    }
}