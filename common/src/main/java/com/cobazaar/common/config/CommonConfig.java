package com.cobazaar.common.config;

import org.springframework.context.annotation.Configuration;

/**
 * 公共配置类占位符
 * Redis配置建议在具体使用Redis的模块中配置
 * 避免在common模块中引入Redis依赖导致的自动装配问题
 * @author huanghongjia
 */
@Configuration
public class CommonConfig {
    // 此类作为一个标记配置类
    // Redis相关配置请在各业务模块中单独配置
}