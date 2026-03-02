package com.cobazaar.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全配置属性类
 * 管理认证相关的配置，包括白名单路径等
 * 配置前缀：security
 *
 * @author cobazaar
 */
@Configuration
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityConfigProperties {

    /**
     * Swagger相关路径白名单
     * 用于放行Swagger UI和API文档相关的路径
     * 示例：/swagger-ui.html, /v3/api-docs/**
     */
    private List<String> swaggerPaths;

    /**
     * 健康检查相关路径白名单
     * 用于放行Spring Boot Actuator监控相关的路径
     * 示例：/actuator/**
     */
    private List<String> healthPaths;

    /**
     * 自定义白名单路径
     * 用于放行业务相关的公共接口
     * 示例：/auth/login, /auth/refresh
     */
    private List<String> customWhitelistPaths;

    /**
     * 获取所有白名单路径
     * 合并swaggerPaths、healthPaths和customWhitelistPaths中的所有路径
     * @return 白名单路径列表，若所有配置项都为null则返回空列表
     */
    public List<String> getAllWhitelistPaths() {
        List<String> allPaths = new ArrayList<>();
        if (swaggerPaths != null) {
            allPaths.addAll(swaggerPaths);
        }
        if (healthPaths != null) {
            allPaths.addAll(healthPaths);
        }
        if (customWhitelistPaths != null) {
            allPaths.addAll(customWhitelistPaths);
        }
        return allPaths;
    }
}