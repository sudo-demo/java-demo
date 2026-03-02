package com.cobazaar.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
//@Profile({"dev", "test"})

/**
 * Knife4j 配置：自动读取 context-path 作为接口前缀
 */
@Configuration
public class Knife4jConfig {

    // 核心：读取微服务的 context-path 配置（无需手动配置前缀）
    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Value("${spring.application.name:system-service}")
    private String serviceName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 自动给文档接口添加 context-path 前缀
                .servers(Collections.singletonList(new Server().url(contextPath)))
                .info(new Info()
                        .title("Cobazaar" + serviceName + "API文档")
                        .description("基于OpenAPI 3.0注解的接口文档，适配Knife4j 4.4.0")
                        .version("1.0.0")
                        .contact(new Contact().name("研发团队").email("dev@cobazaar.com")));
    }
}
