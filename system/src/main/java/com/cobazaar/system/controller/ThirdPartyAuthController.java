package com.cobazaar.system.controller;

import com.cobazaar.common.result.Result;
import com.cobazaar.system.entity.ThirdPartyAuth;
import com.cobazaar.system.service.ThirdPartyAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;

/**
 * 第三方认证管理控制器
 * 提供第三方认证相关的RESTful API接口
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Tag(name = "第三方认证管理")
@RestController
@RequestMapping("/third-party-auth")
@RequiredArgsConstructor
public class ThirdPartyAuthController {

    private final ThirdPartyAuthService thirdPartyAuthService;

    /**
     * 绑定第三方账号
     *
     * @param bindDTO 第三方认证绑定信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "绑定第三方账号", description = "绑定用户的第三方账号")
    public Result<Boolean> bindThirdPartyAuth(@Parameter(name = "bindDTO", description = "第三方认证绑定信息", required = true) @Valid @RequestBody com.cobazaar.system.dto.ThirdPartyAuthBindDTO bindDTO) {
        boolean result = thirdPartyAuthService.bindThirdPartyAuth(bindDTO);
        return Result.success(result);
    }


    /**
     * 解绑第三方账号
     *
     * @param id 第三方认证ID
     * @return 操作结果
     */
    @PostMapping("/unbind/{id}")
    @Operation(summary = "解绑第三方账号", description = "解绑用户的第三方账号")
    public Result<Boolean> unbindThirdPartyAuth(@Parameter(name = "id", description = "第三方认证ID", required = true) @PathVariable Long id) {
        thirdPartyAuthService.unbindThirdPartyAuth(id);
        return Result.success(true);
    }

    /**
     * 根据用户ID获取绑定的第三方账号列表
     *
     * @param userId 用户ID
     * @return 第三方认证列表
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户绑定的第三方账号", description = "根据用户ID获取绑定的第三方账号列表")
    public Result<List<com.cobazaar.system.vo.ThirdPartyAuthVO>> getByUserId(@Parameter(name = "userId", description = "用户ID", required = true) @PathVariable Long userId) {
        List<com.cobazaar.system.vo.ThirdPartyAuthVO> thirdPartyAuths = thirdPartyAuthService.getByUserId(userId);
        return Result.success(thirdPartyAuths);
    }

    /**
     * 刷新访问令牌
     *
     * @param id 第三方认证ID
     * @param newAccessToken 新的访问令牌
     * @param newRefreshToken 新的刷新令牌
     * @param expireTime 过期时间
     * @return 操作结果
     */
    @PostMapping("/refreshToken/{id}")
    @Operation(summary = "刷新访问令牌", description = "刷新第三方账号的访问令牌")
    public Result<Boolean> refreshToken(
            @Parameter(name = "id", description = "第三方认证ID", required = true) @PathVariable Long id,
            @Parameter(name = "newAccessToken", description = "新的访问令牌", required = true) @RequestParam String newAccessToken,
            @Parameter(name = "newRefreshToken", description = "新的刷新令牌", required = true) @RequestParam String newRefreshToken,
            @Parameter(name = "expireTime", description = "过期时间", required = true) @RequestParam String expireTime) {
        LocalDateTime expireDateTime = LocalDateTime.parse(expireTime);
        thirdPartyAuthService.refreshToken(id, newAccessToken, newRefreshToken, expireDateTime);
        return Result.success(true);
    }

    /**
     * 检查第三方账号是否已绑定
     *
     * @param platformType 平台类型
     * @param platformUserId 平台用户ID
     * @return 检查结果
     */
    @GetMapping("/check")
    @Operation(summary = "检查第三方账号是否已绑定", description = "检查指定平台的账号是否已绑定")
    public Result<Boolean> checkBound(
            @Parameter(name = "platformType", description = "平台类型", required = true) @RequestParam Integer platformType,
            @Parameter(name = "platformUserId", description = "平台用户ID", required = true) @RequestParam String platformUserId) {
        boolean isBound = thirdPartyAuthService.isBound(platformType, platformUserId);
        return Result.success(isBound);
    }
}
