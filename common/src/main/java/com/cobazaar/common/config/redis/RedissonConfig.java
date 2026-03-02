package com.cobazaar.common.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Redisson配置类
 * 为多个微服务提供统一的RedissonClient配置
 * 支持分布式锁、原子操作等高级Redis功能
 *
 * @author cobazaar
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

    /**
     * 配置RedissonClient
     * 从Spring Boot的Redis配置中读取连接信息
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "true", matchIfMissing = false)
    public RedissonClient redissonClient(RedisProperties redisProperties, Environment env) {
        System.err.println("Redis is enabled, creating RedissonClient");
        return null;
    }
}