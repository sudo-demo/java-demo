package com.cobazaar.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 网关健康检查和测试控制器
 * 用于验证网关功能是否正常工作
 *
 * @author Cobazaar开发团队
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
public class GatewayTestController {

    /**
     * 健康检查接口
     * @return 健康状态信息
     */
    @GetMapping("/health")
    public Mono<Map<String, Object>> healthCheck() {
        log.info("执行健康检查");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "gateway");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("message", "网关服务运行正常");
        
        return Mono.just(response);
    }

    /**
     * 测试参数接口
     * @param name 测试参数
     * @return 测试结果
     */
    @GetMapping("/echo")
    public Mono<Map<String, String>> echo(
            @RequestParam(defaultValue = "World") String name) {
        log.info("执行回声测试，参数: {}", name);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello " + name + "!");
        response.put("from", "Gateway Service");
        response.put("time", LocalDateTime.now().toString());
        
        return Mono.just(response);
    }

    /**
     * 获取网关信息
     * @return 网关基本信息
     */
    @GetMapping("/info")
    public Mono<Map<String, Object>> getGatewayInfo() {
        log.info("获取网关信息");
        
        Map<String, Object> response = new HashMap<>();
        response.put("serviceName", "gateway");
        response.put("version", "1.0.0");
        response.put("description", "Cobazaar 微服务网关");
        response.put("features", new String[]{"路由转发", "负载均衡", "限流熔断", "API文档"});
        response.put("timestamp", LocalDateTime.now().toString());
        
        return Mono.just(response);
    }
}