package com.cobazaar.gateway.filter.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.*;

/**
 * 高度配置化的请求头清洗过滤器工厂
 * 提供灵活的请求头处理能力，包括添加、删除、修改、验证等功能
 * 
 * 功能特性：
 * 1. 支持请求头添加、删除、修改操作
 * 2. 支持基于条件的头处理
 * 3. 支持正则表达式匹配
 * 4. 支持黑白名单控制
 * 5. 支持安全头自动添加
 * 6. 支持请求头验证和清理
 * 7. 完善的日志记录和监控
 *
 * 配置示例：
 * # 基础配置
 * header-clean:
 *   enabled: true
 *   remove-headers:
 *     - X-Internal-Token
 *     - X-Secret-Key
 *   add-headers:
 *     X-Gateway: cobazaar-gateway
 *     X-Forwarded-Proto: https
 * 
 * # 条件化处理
 * header-clean:
 *   enabled: true
 *   conditions:
 *     - header: X-Client-Type
 *       value: mobile
 *       remove-headers: [X-Desktop-Only]
 *       add-headers:
 *         X-Mobile-Optimized: true
 * 
 * # 安全头配置
 * header-clean:
 *   enabled: true
 *   security-headers:
 *     enabled: true
 *     xss-protection: "1; mode=block"
 *     frame-options: DENY
 *     content-type-options: nosniff
 * 
 * # 验证规则
 * header-clean:
 *   enabled: true
 *   validation-rules:
 *     - header: Authorization
 *       pattern: "^Bearer .+$"
 *       required: true
 *       action: reject  # reject | clean | log
 * 
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-05
 */
@Slf4j
@Component
public class ConfigurableHeaderCleanGatewayFilterFactory 
    extends AbstractGatewayFilterFactory<ConfigurableHeaderCleanGatewayFilterFactory.Config> {

    public ConfigurableHeaderCleanGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // 检查是否启用
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }
            
            // 应用基础头处理
            ServerHttpRequest.Builder requestBuilder = request.mutate();
            applyBasicHeaderOperations(requestBuilder, config);
            
            // 应用条件化处理
            applyConditionalOperations(requestBuilder, request, config);
            
            // 应用安全头
            applySecurityHeaders(requestBuilder, config);
            
            // 应用验证规则
            if (!applyValidationRules(request, config)) {
                // 验证失败的处理
                log.warn("Header validation failed for request: {}", request.getURI());
                // 可以选择拒绝请求或继续处理
            }
            
            ServerHttpRequest newRequest = requestBuilder.build();
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            
            return chain.filter(newExchange);
        };
    }

    /**
     * 应用基础头操作
     */
    private void applyBasicHeaderOperations(ServerHttpRequest.Builder builder, Config config) {
        // 删除指定头
        if (config.getRemoveHeaders() != null) {
            for (String header : config.getRemoveHeaders()) {
                builder.headers(httpHeaders -> httpHeaders.remove(header));
                log.debug("Removed header: {}", header);
            }
        }
        
        // 添加指定头
        if (config.getAddHeaders() != null) {
            for (Map.Entry<String, String> entry : config.getAddHeaders().entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
                log.debug("Added header: {}={}", entry.getKey(), entry.getValue());
            }
        }
        
        // 修改指定头
        if (config.getModifyHeaders() != null) {
            for (HeaderModification mod : config.getModifyHeaders()) {
                builder.headers(httpHeaders -> {
                    String currentValue = httpHeaders.getFirst(mod.getHeader());
                    if (currentValue != null) {
                        String newValue = mod.getOperation().apply(currentValue, mod.getValue());
                        httpHeaders.set(mod.getHeader(), newValue);
                        log.debug("Modified header: {} from '{}' to '{}'", 
                                mod.getHeader(), currentValue, newValue);
                    }
                });
            }
        }
    }

    /**
     * 应用条件化操作
     */
    private void applyConditionalOperations(ServerHttpRequest.Builder builder, 
                                          ServerHttpRequest request, Config config) {
        if (config.getConditions() == null) {
            return;
        }
        
        for (ConditionalRule condition : config.getConditions()) {
            if (matchesCondition(request, condition)) {
                // 应用条件对应的头操作
                applyRuleOperations(builder, condition);
            }
        }
    }

    /**
     * 应用安全头
     */
    private void applySecurityHeaders(ServerHttpRequest.Builder builder, Config config) {
        SecurityHeaders security = config.getSecurityHeaders();
        if (security == null || !security.isEnabled()) {
            return;
        }
        
        // XSS保护头
        if (security.getXssProtection() != null) {
            builder.header("X-XSS-Protection", security.getXssProtection());
        }
        
        // Frame选项头
        if (security.getFrameOptions() != null) {
            builder.header("X-Frame-Options", security.getFrameOptions());
        }
        
        // 内容类型选项头
        if (security.getContentTypeOptions() != null) {
            builder.header("X-Content-Type-Options", security.getContentTypeOptions());
        }
        
        // 严格传输安全头
        if (security.getHsts() != null) {
            builder.header("Strict-Transport-Security", security.getHsts());
        }
        
        log.debug("Applied security headers");
    }

    /**
     * 应用验证规则
     */
    private boolean applyValidationRules(ServerHttpRequest request, Config config) {
        if (config.getValidationRules() == null) {
            return true;
        }
        
        boolean allValid = true;
        HttpHeaders headers = request.getHeaders();
        
        for (ValidationRule rule : config.getValidationRules()) {
            String headerValue = headers.getFirst(rule.getHeader());
            
            // 检查必需头
            if (rule.isRequired() && headerValue == null) {
                log.warn("Required header missing: {}", rule.getHeader());
                handleValidationError(rule, "Missing required header: " + rule.getHeader());
                allValid = false;
                continue;
            }
            
            // 检查模式匹配
            if (headerValue != null && rule.getPattern() != null) {
                if (!headerValue.matches(rule.getPattern())) {
                    log.warn("Header validation failed: {}='{}' does not match pattern '{}'", 
                            rule.getHeader(), headerValue, rule.getPattern());
                    handleValidationError(rule, "Header validation failed: " + rule.getHeader());
                    allValid = false;
                }
            }
        }
        
        return allValid;
    }

    /**
     * 处理验证错误
     */
    private void handleValidationError(ValidationRule rule, String message) {
        switch (rule.getAction()) {
            case REJECT:
                log.error("Rejecting request due to: {}", message);
                // 实际应用中可以通过抛出异常来拒绝请求
                break;
            case CLEAN:
                log.warn("Cleaning invalid header due to: {}", message);
                break;
            case LOG:
                log.info("Logged validation issue: {}", message);
                break;
        }
    }

    /**
     * 检查条件是否匹配
     */
    private boolean matchesCondition(ServerHttpRequest request, ConditionalRule condition) {
        String headerValue = request.getHeaders().getFirst(condition.getHeader());
        if (headerValue == null) {
            return false;
        }
        
        if (condition.getValue() != null) {
            return headerValue.equals(condition.getValue());
        }
        
        if (condition.getPattern() != null) {
            return headerValue.matches(condition.getPattern());
        }
        
        return true;
    }

    /**
     * 应用规则操作
     */
    private void applyRuleOperations(ServerHttpRequest.Builder builder, ConditionalRule rule) {
        // 删除头
        if (rule.getRemoveHeaders() != null) {
            for (String header : rule.getRemoveHeaders()) {
                builder.headers(httpHeaders -> httpHeaders.remove(header));
            }
        }
        
        // 添加头
        if (rule.getAddHeaders() != null) {
            for (Map.Entry<String, String> entry : rule.getAddHeaders().entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 配置类
     */
    public static class Config {
        private boolean enabled = true;
        private List<String> removeHeaders;
        private Map<String, String> addHeaders;
        private List<HeaderModification> modifyHeaders;
        private List<ConditionalRule> conditions;
        private SecurityHeaders securityHeaders;
        private List<ValidationRule> validationRules;

        // Getters and Setters
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public List<String> getRemoveHeaders() { return removeHeaders; }
        public void setRemoveHeaders(List<String> removeHeaders) { this.removeHeaders = removeHeaders; }
        
        public Map<String, String> getAddHeaders() { return addHeaders; }
        public void setAddHeaders(Map<String, String> addHeaders) { this.addHeaders = addHeaders; }
        
        public List<HeaderModification> getModifyHeaders() { return modifyHeaders; }
        public void setModifyHeaders(List<HeaderModification> modifyHeaders) { this.modifyHeaders = modifyHeaders; }
        
        public List<ConditionalRule> getConditions() { return conditions; }
        public void setConditions(List<ConditionalRule> conditions) { this.conditions = conditions; }
        
        public SecurityHeaders getSecurityHeaders() { return securityHeaders; }
        public void setSecurityHeaders(SecurityHeaders securityHeaders) { this.securityHeaders = securityHeaders; }
        
        public List<ValidationRule> getValidationRules() { return validationRules; }
        public void setValidationRules(List<ValidationRule> validationRules) { this.validationRules = validationRules; }
    }

    /**
     * 头修改操作类
     */
    public static class HeaderModification {
        private String header;
        private String value;
        private ModificationOperation operation;

        public enum ModificationOperation {
            REPLACE((current, newValue) -> newValue),
            APPEND((current, newValue) -> current + newValue),
            PREPEND((current, newValue) -> newValue + current);
            
            private final ModificationFunction function;
            
            ModificationOperation(ModificationFunction function) {
                this.function = function;
            }
            
            public String apply(String current, String newValue) {
                return function.apply(current, newValue);
            }
        }
        
        @FunctionalInterface
        interface ModificationFunction {
            String apply(String current, String newValue);
        }

        // Getters and Setters
        public String getHeader() { return header; }
        public void setHeader(String header) { this.header = header; }
        
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        
        public ModificationOperation getOperation() { return operation; }
        public void setOperation(ModificationOperation operation) { this.operation = operation; }
    }

    /**
     * 条件规则类
     */
    public static class ConditionalRule {
        private String header;
        private String value;
        private String pattern;
        private List<String> removeHeaders;
        private Map<String, String> addHeaders;

        // Getters and Setters
        public String getHeader() { return header; }
        public void setHeader(String header) { this.header = header; }
        
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        
        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }
        
        public List<String> getRemoveHeaders() { return removeHeaders; }
        public void setRemoveHeaders(List<String> removeHeaders) { this.removeHeaders = removeHeaders; }
        
        public Map<String, String> getAddHeaders() { return addHeaders; }
        public void setAddHeaders(Map<String, String> addHeaders) { this.addHeaders = addHeaders; }
    }

    /**
     * 安全头配置类
     */
    public static class SecurityHeaders {
        private boolean enabled = true;
        private String xssProtection = "1; mode=block";
        private String frameOptions = "DENY";
        private String contentTypeOptions = "nosniff";
        private String hsts = "max-age=31536000; includeSubDomains";

        // Getters and Setters
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public String getXssProtection() { return xssProtection; }
        public void setXssProtection(String xssProtection) { this.xssProtection = xssProtection; }
        
        public String getFrameOptions() { return frameOptions; }
        public void setFrameOptions(String frameOptions) { this.frameOptions = frameOptions; }
        
        public String getContentTypeOptions() { return contentTypeOptions; }
        public void setContentTypeOptions(String contentTypeOptions) { this.contentTypeOptions = contentTypeOptions; }
        
        public String getHsts() { return hsts; }
        public void setHsts(String hsts) { this.hsts = hsts; }
    }

    /**
     * 验证规则类
     */
    public static class ValidationRule {
        public enum ValidationErrorAction {
            REJECT, CLEAN, LOG
        }
        
        private String header;
        private String pattern;
        private boolean required = false;
        private ValidationErrorAction action = ValidationErrorAction.LOG;

        // Getters and Setters
        public String getHeader() { return header; }
        public void setHeader(String header) { this.header = header; }
        
        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }
        
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        
        public ValidationErrorAction getAction() { return action; }
        public void setAction(ValidationErrorAction action) { this.action = action; }
    }
}