package com.cobazaar.common.security;

import com.cobazaar.common.enums.ResponseEnum;
import com.cobazaar.common.exception.BizException;
import com.cobazaar.common.result.Result;
import com.cobazaar.common.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一认证入口点
 * 处理未认证请求的统一响应
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-06
 */
@Slf4j
@Component
public class CommonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        log.warn("未认证访问: URI={}, 异常类型={}", request.getRequestURI(), authException.getClass().getSimpleName());
        
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        
        Result<Void> result;
        
        if (authException instanceof AccountExpiredException) {
            result = Result.fail(ResponseEnum.UNAUTHORIZED.getCode(), "账户已过期");
        } else if (authException instanceof CredentialsExpiredException) {
            result = Result.fail(ResponseEnum.UNAUTHORIZED.getCode(), "凭证已过期");
        } else if (authException instanceof DisabledException) {
            result = Result.fail(ResponseEnum.UNAUTHORIZED.getCode(), "账户已被禁用");
        } else if (authException instanceof LockedException) {
            result = Result.fail(ResponseEnum.UNAUTHORIZED.getCode(), "账户已被锁定");
        } else if (authException instanceof BadCredentialsException) {
            result = Result.fail(ResponseEnum.UNAUTHORIZED.getCode(), "用户名或密码错误");
        } else if (authException instanceof UsernameNotFoundException) {
            result = Result.fail(ResponseEnum.UNAUTHORIZED.getCode(), "用户不存在");
        } else if (authException instanceof InsufficientAuthenticationException) {
            result = Result.fail(ResponseEnum.UNAUTHORIZED.getCode(), "认证信息不完整");
        } else {
            result = Result.fail(ResponseEnum.UNAUTHORIZED.getCode(), "未授权访问");
        }
        
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}