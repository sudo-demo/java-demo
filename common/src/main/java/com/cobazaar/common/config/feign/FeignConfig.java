package com.cobazaar.common.config.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign配置类
 * 用于配置Feign的全局设置，如请求拦截器、超时时间等
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-12
 */
@Configuration
public class FeignConfig {
    
    /**
     * Feign配置类
     * 注意：FeignHeaderInterceptor已经通过@Component注解自动注册为bean
     */
    
    /**
     * 可以在这里添加其他Feign相关的配置，如：
     * - 超时时间设置
     * - 重试策略
     * - 日志级别
     * - 编码器/解码器
     */
}
