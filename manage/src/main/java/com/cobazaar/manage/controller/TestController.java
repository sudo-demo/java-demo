package com.cobazaar.manage.controller;

import com.cobazaar.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@Tag(name = "测试管理", description = "提供测试相关的RESTful API接口")
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 测试接口
     * @return 测试结果
     */
    @GetMapping("/hello")
    @Operation(summary = "测试接口", description = "测试管理服务是否正常运行")
    public Result<String> hello() {
        return Result.success("Hello from manage service!");
    }

    /**
     * 健康检查接口
     * @return 健康状态
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查管理服务的健康状态")
    public Result<String> health() {
        return Result.success("Manage service is healthy!");
    }
}
