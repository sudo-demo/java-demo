package com.cobazaar.system.controller;

import com.cobazaar.common.result.Result;
import com.cobazaar.system.dto.ChangePasswordDTO;
import com.cobazaar.system.dto.LoginDTO;
import com.cobazaar.system.dto.RegisterDTO;
import com.cobazaar.system.service.AuthService;
import com.cobazaar.system.service.CaptchaService;
import com.cobazaar.system.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 认证控制器
 * 处理用户登录、登出、刷新令牌等认证相关的请求
 * 
 * @author cobazaar
 */
@Tag(name = "认证管理", description = "处理用户登录、登出、刷新令牌等认证相关的请求")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final CaptchaService captchaService;

    /**
     * 生成验证码
     * @param response HTTP响应对象
     * @param uuid 验证码唯一标识
     * @throws IOException IO异常
     */
    @GetMapping("/captcha")
    @Operation(summary = "生成验证码", description = "生成验证码图片并返回")
    public void getCaptcha(@Parameter(name = "uuid", description = "验证码唯一标识", required = true) @RequestParam(name = "uuid") String uuid, HttpServletResponse response) throws IOException {
        captchaService.generateCaptcha(response, uuid);
    }

    /**
     * 用户登录
     * @param loginDTO 登录请求参数
     * @return 登录响应
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "根据用户名和密码进行登录认证")
    public Result<LoginVO> login(@Parameter(name = "loginDTO", description = "登录请求参数", required = true) @Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return Result.success(loginVO);
    }

    /**
     * 用户登出
     * @return 登出结果
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户退出登录")
    public Result<?> logout() {
        authService.logout();
        return Result.success("登出成功");
    }

    /**
     * 刷新令牌
     * @param refreshToken 刷新令牌
     * @return 新访问令牌
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public Result<LoginVO> refreshToken(@Parameter(name = "refreshToken", description = "刷新令牌", required = true) @RequestBody String refreshToken) {
        LoginVO loginVO = authService.refreshToken(refreshToken);
        return Result.success(loginVO);
    }

    /**
     * 获取当前用户信息
     * @return 当前用户信息
     */
    @PostMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public Result<?> getCurrentUser() {
        Object userInfo = authService.getCurrentUser();
        return Result.success(userInfo);
    }

    /**
     * 用户注册
     * @param registerDTO 注册请求参数
     * @return 注册响应
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册")
    public Result<LoginVO> register(@Parameter(name = "registerDTO", description = "注册请求参数", required = true) @Valid @RequestBody RegisterDTO registerDTO) {
        LoginVO loginVO = authService.register(registerDTO);
        return Result.success(loginVO);
    }

    /**
     * 修改密码
     * @param changePasswordDTO 修改密码请求参数
     * @return 修改结果
     */
    @PostMapping("/changePassword")
    @Operation(summary = "修改密码", description = "用户修改密码")
    public Result<?> changePassword(@Parameter(name = "changePasswordDTO", description = "修改密码请求参数", required = true) @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        // 验证新密码和确认密码是否一致
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            return Result.error("两次输入的新密码不一致");
        }
        boolean result = authService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        if (result) {
            return Result.success("密码修改成功");
        } else {
            return Result.error("密码修改失败，旧密码不正确");
        }
    }
}
