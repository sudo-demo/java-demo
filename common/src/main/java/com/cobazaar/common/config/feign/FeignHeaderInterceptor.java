package com.cobazaar.common.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Feign请求拦截器
 * 用于在微服务间传递Header参数，如认证Token、自定义Header等
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-12
 */
@Slf4j
@Component
public class FeignHeaderInterceptor implements RequestInterceptor {
    
    @Override
    public void apply(RequestTemplate template) {
        log.debug("Feign请求拦截器开始执行: URI={}", template.url());
        
        // 从当前请求上下文获取原始请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) 
            RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.debug("从RequestContextHolder获取到原始请求: URI={}", request.getRequestURI());
            
            // 复制所有Header
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    // 跳过Content-Length等可能导致问题的Header
                    if (!"Content-Length".equals(headerName)) {
                        String headerValue = request.getHeader(headerName);
                        template.header(headerName, headerValue);
                        log.debug("传递Header: {}={}", headerName, headerValue != null ? "******" : "null");
                    }
                }
            }
            
            // 也可以从URL参数中获取token并传递
            String token = request.getParameter("token");
            if (token != null && !token.isEmpty()) {
                template.header("Authorization", "Bearer " + token);
                log.debug("从URL参数获取并传递Token");
            }
        } else {
            log.debug("RequestContextHolder中无请求上下文，跳过Header传递");
        }
        
        log.debug("Feign请求拦截器执行完成");
    }
}
