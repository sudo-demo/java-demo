package com.cobazaar.common.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Redis环境配置类
 * 用于配置Redis key的环境前缀，实现不同环境数据隔离
 * 配置示例：
 * spring:
 *   redis:
 *     env:
 *       prefix: dev  # 开发环境前缀
 *       enabled: true # 是否启用环境前缀
 *
 * @author cobazaar
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis.env")
public class RedisEnvConfig {

    /**
     * 环境前缀，如果配置了前缀则自动启用环境前缀功能
     * 默认为空（不添加前缀）
     */
    private String prefix = "";

    /**
     * 获取完整的环境前缀（包含分隔符）
     * @return 完整的环境前缀，格式为 "prefix:" 或 ""（当未配置前缀时）
     */
    public String getFullPrefix() {
        if (prefix == null || prefix.trim().isEmpty()) {
            return "";
        }
        return prefix.trim() + ":";
    }

    /**
     * 为指定的key添加环境前缀
     * @param key 原始key
     * @return 添加环境前缀后的key
     */
    public String addPrefix(String key) {
        if (key == null || key.isEmpty()) {
            return key;
        }
        return getFullPrefix() + key;
    }

    /**
     * 移除key的环境前缀（如果存在）
     * @param key 带前缀的key
     * @return 移除前缀后的原始key
     */
    public String removePrefix(String key) {
        if (key == null || key.isEmpty()) {
            return key;
        }
        
        String fullPrefix = getFullPrefix();
        if (fullPrefix.isEmpty()) {
            return key;
        }
        
        if (key.startsWith(fullPrefix)) {
            return key.substring(fullPrefix.length());
        }
        
        return key;
    }

    /**
     * 判断是否启用了环境前缀功能
     * @return 当配置了前缀时返回true，否则返回false
     */
    public boolean isEnabled() {
        return prefix != null && !prefix.trim().isEmpty();
    }

    // Getter和Setter方法
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}