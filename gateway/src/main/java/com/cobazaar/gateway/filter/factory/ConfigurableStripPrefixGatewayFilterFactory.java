package com.cobazaar.gateway.filter.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * 高度配置化的路由前缀剥离过滤器工厂
 * 支持多种配置方式的路径前缀移除功能
 * 
 * 功能特性：
 * 1. 支持固定前缀剥离
 * 2. 支持动态前缀剥离（基于请求头、参数等）
 * 3. 支持正则表达式匹配
 * 4. 支持条件化执行
 * 5. 支持黑白名单控制
 * 6. 完善的日志记录和监控
 *
 * 配置示例：
 * # 固定前缀剥离
 * strip-prefix:
 *   parts: 1
 *   enabled: true
 * 
 * # 动态前缀剥离
 * strip-prefix:
 *   dynamic: true
 *   header-name: X-Strip-Prefix
 *   parameter-name: strip_prefix
 * 
 * # 正则表达式模式
 * strip-prefix:
 *   regex-pattern: "^/api/v\\d+/"
 * 
 * # 条件化执行
 * strip-prefix:
 *   enabled: true
 *   parts: 1
 *   condition:
 *     header: X-Enable-Strip
 *     value: "true"
 * 
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-05
 */
@Slf4j
@Component
public class ConfigurableStripPrefixGatewayFilterFactory 
    extends AbstractGatewayFilterFactory<ConfigurableStripPrefixGatewayFilterFactory.Config> {

    public ConfigurableStripPrefixGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("parts", "enabled", "dynamic", "headerName", "parameterName");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // 检查是否启用
            if (!config.isEnabled()) {
                log.debug("StripPrefix filter disabled for request: {}", request.getURI());
                return chain.filter(exchange);
            }
            
            // 检查执行条件
            if (!shouldExecute(request, config)) {
                log.debug("StripPrefix condition not met for request: {}", request.getURI());
                return chain.filter(exchange);
            }
            
            // 检查黑白名单
            if (!isAllowed(request, config)) {
                log.debug("StripPrefix not allowed for request: {}", request.getURI());
                return chain.filter(exchange);
            }

            String path = request.getURI().getPath();
            String newPath = stripPrefix(path, request, config);
            
            if (!path.equals(newPath)) {
                URI newUri = UriComponentsBuilder.fromUri(request.getURI())
                        .replacePath(newPath)
                        .build(true)
                        .toUri();
                
                ServerHttpRequest newRequest = request.mutate()
                        .uri(newUri)
                        .build();
                
                ServerWebExchange newExchange = exchange.mutate()
                        .request(newRequest)
                        .build();
                
                log.info("StripPrefix: {} -> {}, parts: {}, dynamic: {}", 
                        path, newPath, config.getParts(), config.isDynamic());
                
                return chain.filter(newExchange);
            }
            
            return chain.filter(exchange);
        };
    }

    /**
     * 执行前缀剥离逻辑
     */
    private String stripPrefix(String path, ServerHttpRequest request, Config config) {
        if (config.isDynamic()) {
            return stripDynamicPrefix(path, request, config);
        } else if (config.getRegexPattern() != null) {
            return stripRegexPrefix(path, config);
        } else {
            return stripFixedPrefix(path, config);
        }
    }

    /**
     * 固定前缀剥离
     */
    private String stripFixedPrefix(String path, Config config) {
        String[] parts = path.split("/");
        int partsToRemove = Math.min(config.getParts(), parts.length - 1);
        
        if (partsToRemove <= 0) {
            return path;
        }
        
        StringBuilder newPath = new StringBuilder();
        for (int i = partsToRemove + 1; i < parts.length; i++) {
            newPath.append("/").append(parts[i]);
        }
        
        return newPath.length() > 0 ? newPath.toString() : "/";
    }

    /**
     * 动态前缀剥离
     */
    private String stripDynamicPrefix(String path, ServerHttpRequest request, Config config) {
        String prefixLengthStr = null;
        
        // 从请求头获取
        if (config.getHeaderName() != null) {
            prefixLengthStr = request.getHeaders().getFirst(config.getHeaderName());
        }
        
        // 从查询参数获取
        if (prefixLengthStr == null && config.getParameterName() != null) {
            prefixLengthStr = request.getQueryParams().getFirst(config.getParameterName());
        }
        
        if (prefixLengthStr != null) {
            try {
                int partsToRemove = Integer.parseInt(prefixLengthStr);
                Config dynamicConfig = new Config();
                dynamicConfig.setParts(partsToRemove);
                return stripFixedPrefix(path, dynamicConfig);
            } catch (NumberFormatException e) {
                log.warn("Invalid prefix length value: {}", prefixLengthStr);
            }
        }
        
        return path;
    }

    /**
     * 正则表达式前缀剥离
     */
    private String stripRegexPrefix(String path, Config config) {
        return path.replaceFirst(config.getRegexPattern(), "/");
    }

    /**
     * 判断是否应该执行剥离
     */
    private boolean shouldExecute(ServerHttpRequest request, Config config) {
        Condition condition = config.getCondition();
        if (condition == null) {
            return true;
        }
        
        String headerValue = request.getHeaders().getFirst(condition.getHeader());
        return headerValue != null && headerValue.equals(condition.getValue());
    }

    /**
     * 判断是否允许执行剥离
     */
    private boolean isAllowed(ServerHttpRequest request, Config config) {
        // 黑名单检查
        if (config.getBlacklist() != null) {
            for (String pattern : config.getBlacklist()) {
                if (request.getURI().getPath().matches(pattern)) {
                    return false;
                }
            }
        }
        
        // 白名单检查
        if (config.getWhitelist() != null) {
            boolean matched = false;
            for (String pattern : config.getWhitelist()) {
                if (request.getURI().getPath().matches(pattern)) {
                    matched = true;
                    break;
                }
            }
            return matched;
        }
        
        return true;
    }

    /**
     * 配置类
     */
    public static class Config {
        /** 要移除的路径部分数量 */
        private int parts = 1;
        
        /** 是否启用过滤器 */
        private boolean enabled = true;
        
        /** 是否启用动态模式 */
        private boolean dynamic = false;
        
        /** 动态模式下的请求头名称 */
        private String headerName;
        
        /** 动态模式下的查询参数名称 */
        private String parameterName;
        
        /** 正则表达式模式 */
        private String regexPattern;
        
        /** 执行条件 */
        private Condition condition;
        
        /** 黑名单路径模式 */
        private List<String> blacklist;
        
        /** 白名单路径模式 */
        private List<String> whitelist;

        // Getters and Setters
        public int getParts() { return parts; }
        public void setParts(int parts) { this.parts = parts; }
        
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public boolean isDynamic() { return dynamic; }
        public void setDynamic(boolean dynamic) { this.dynamic = dynamic; }
        
        public String getHeaderName() { return headerName; }
        public void setHeaderName(String headerName) { this.headerName = headerName; }
        
        public String getParameterName() { return parameterName; }
        public void setParameterName(String parameterName) { this.parameterName = parameterName; }
        
        public String getRegexPattern() { return regexPattern; }
        public void setRegexPattern(String regexPattern) { this.regexPattern = regexPattern; }
        
        public Condition getCondition() { return condition; }
        public void setCondition(Condition condition) { this.condition = condition; }
        
        public List<String> getBlacklist() { return blacklist; }
        public void setBlacklist(List<String> blacklist) { this.blacklist = blacklist; }
        
        public List<String> getWhitelist() { return whitelist; }
        public void setWhitelist(List<String> whitelist) { this.whitelist = whitelist; }
    }

    /**
     * 条件配置类
     */
    public static class Condition {
        /** 条件头名称 */
        private String header;
        
        /** 条件值 */
        private String value;

        // Getters and Setters
        public String getHeader() { return header; }
        public void setHeader(String header) { this.header = header; }
        
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
}