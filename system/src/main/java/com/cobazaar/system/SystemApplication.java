package com.cobazaar.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 系统管理服务启动类
 * 
 * @author cobazaar
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.cobazaar.system", "com.cobazaar.common"})
@MapperScan("com.cobazaar.system.mapper")
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}