package com.cobazaar.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关测试控制器
 * 用于验证网关配置是否正常工作
 *
 * @author Cobazaar开发团队
 */
@RestController
@RequestMapping("/gateway")
public class GatewayController {

    /**
     * 网关健康检查接口
     * @return 健康状态信息
     */
    @GetMapping("/health")
    public String health() {
        return "Gateway service is running normally";
    }

    /**
     * 网关信息接口
     * @return 网关基本信息
     */
    @GetMapping("/info")
    public Object getInfo() {
        return new GatewayInfo("Cobazaar Gateway", "1.0.0", "Running");
    }

    /**
     * 内部类：网关信息
     */
    public static class GatewayInfo {
        private String name;
        private String version;
        private String status;

        public GatewayInfo(String name, String version, String status) {
            this.name = name;
            this.version = version;
            this.status = status;
        }

        public String getName() { return name; }
        public String getVersion() { return version; }
        public String getStatus() { return status; }
    }
}