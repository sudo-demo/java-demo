package com.cobazaar.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate自动配置类
 * 提供RestTemplate的默认配置
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建RestTemplate实例
     */
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(simpleClientHttpRequestFactory());
        return restTemplate;
    }

    /**
     * 配置HTTP请求工厂
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时时间：5秒
        factory.setConnectTimeout(5000);
        // 读取超时时间：10秒
        factory.setReadTimeout(10000);
        return factory;
    }
}