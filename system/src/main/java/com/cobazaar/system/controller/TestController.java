package com.cobazaar.system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "测试")
@RestController
public class TestController {

    @GetMapping("/test")
    @Operation(summary = "测试接口", description = "测试系统服务是否正常运行")
    public String test() {
        return "System service is running!";
    }
}