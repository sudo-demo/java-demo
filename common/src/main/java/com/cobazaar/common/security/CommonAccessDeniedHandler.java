package com.cobazaar.common.security;

import com.cobazaar.common.enums.ResponseEnum;
import com.cobazaar.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一访问拒绝处理器
 * 处理权限不足的统一响应
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-06
 */
@Slf4j
@Component
public class CommonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        log.warn("权限不足访问: URI={}, 用户={}", request.getRequestURI(), 
                 request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous");
        
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        
        Result<Void> result = Result.fail(ResponseEnum.FORBIDDEN.getCode(), "权限不足，无法访问该资源");
        
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}