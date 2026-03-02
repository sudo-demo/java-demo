package com.cobazaar.system.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * 测试Redis配置
 * 用于排除Redisson自动配置
 */
@Configuration
@EnableAutoConfiguration(exclude = {
    org.redisson.spring.starter.RedissonAutoConfiguration.class
})
public class TestRedisConfig {
}
