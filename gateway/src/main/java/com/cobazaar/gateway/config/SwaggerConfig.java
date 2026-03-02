package com.cobazaar.gateway.config;

import org.springframework.context.annotation.Configuration;

/**
 * Swagger 配置
 * 用于网关聚合所有服务的 Swagger 文档
 */
@Configuration
public class SwaggerConfig {

    // 路由配置已移至 application.yml 文件
    // 使用 application.yml 中的路由配置，确保 StripPrefix 过滤器生效

}
