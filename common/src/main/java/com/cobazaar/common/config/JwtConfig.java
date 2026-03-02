package com.cobazaar.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类
 * 用于加载和管理JWT相关的配置项
 *
 * @author cobazaar
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * JWT密钥
     */
    private String secret = "default_secret_key_12345";
    
    /**
     * JWT过期时间（秒）
     */
    private long expire = 3600;
    
    /**
     * JWT签名算法
     */
    private String algorithm = "HS256";
    
    /**
     * JWT请求头名称
     */
    private String header = "Authorization";
    
    /**
     * JWT请求头前缀
     */
    private String prefix = "Bearer";

    /**
     * 获取完整的JWT请求头前缀，包含空格
     * @return 完整的JWT请求头前缀
     */
    public String getFullPrefix() {
        return prefix + " ";
    }

    /**
     * 验证配置是否有效
     * @return 配置是否有效
     */
    public boolean isValid() {
        return secret != null && !secret.isEmpty() && expire > 0 && algorithm != null && !algorithm.isEmpty();
    }

}
