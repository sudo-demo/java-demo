package com.cobazaar.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 管理服务主应用类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.cobazaar.manage", "com.cobazaar.common"})  // 扫描管理服务和公共模块的组件
public class ManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
    }
}
