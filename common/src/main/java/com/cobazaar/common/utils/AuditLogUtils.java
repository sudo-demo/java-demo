package com.cobazaar.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 审计日志工具类
 * 用于记录操作审计记录
 * 
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Slf4j
public final class AuditLogUtils {

    private AuditLogUtils() {
        throw new UnsupportedOperationException("AuditLogUtils is a utility class and cannot be instantiated");
    }

    // ==================== 操作类型 ====================

    public static final String OPERATION_CREATE = "CREATE";
    public static final String OPERATION_UPDATE = "UPDATE";
    public static final String OPERATION_DELETE = "DELETE";
    public static final String OPERATION_QUERY = "QUERY";
    public static final String OPERATION_LOGIN = "LOGIN";
    public static final String OPERATION_LOGOUT = "LOGOUT";
    public static final String OPERATION_IMPORT = "IMPORT";
    public static final String OPERATION_EXPORT = "EXPORT";

    // ==================== 日志级别 ====================

    public static final String LEVEL_INFO = "INFO";
    public static final String LEVEL_WARN = "WARN";
    public static final String LEVEL_ERROR = "ERROR";
    public static final String LEVEL_DEBUG = "DEBUG";

    // ==================== 日志记录 ====================

    /**
     * 记录审计日志
     * 
     * @param userId      用户ID
     * @param username    用户名
     * @param operation   操作类型
     * @param module      模块名称
     * @param description 操作描述
     * @param success     是否成功
     * @param ipAddress   IP地址
     * @param parameters  操作参数
     */
    public static void recordAuditLog(Long userId, String username, String operation, 
                                    String module, String description, boolean success, 
                                    String ipAddress, Map<String, Object> parameters) {
        try {
            // 构建日志内容
            Map<String, Object> logContent = new ConcurrentHashMap<>();
            logContent.put("timestamp", System.currentTimeMillis());
            logContent.put("userId", userId);
            logContent.put("username", username);
            logContent.put("operation", operation);
            logContent.put("module", module);
            logContent.put("description", description);
            logContent.put("success", success);
            logContent.put("ipAddress", ipAddress);
            logContent.put("parameters", parameters);
            logContent.put("level", success ? LEVEL_INFO : LEVEL_ERROR);

            // 记录日志（这里可以根据实际情况存储到数据库或日志系统）
            logAuditLog(logContent);

        } catch (Exception e) {
            log.error("记录审计日志失败", e);
        }
    }

    /**
     * 记录登录日志
     * 
     * @param userId      用户ID
     * @param username    用户名
     * @param success     是否成功
     * @param ipAddress   IP地址
     * @param userAgent   用户代理
     * @param errorMessage 错误信息
     */
    public static void recordLoginLog(Long userId, String username, boolean success, 
                                    String ipAddress, String userAgent, String errorMessage) {
        Map<String, Object> parameters = new ConcurrentHashMap<>();
        parameters.put("userAgent", userAgent);
        parameters.put("errorMessage", errorMessage);

        recordAuditLog(userId, username, OPERATION_LOGIN, "系统管理", 
                     success ? "用户登录成功" : "用户登录失败", success, 
                     ipAddress, parameters);
    }

    /**
     * 记录操作日志
     * 
     * @param userId      用户ID
     * @param username    用户名
     * @param operation   操作类型
     * @param module      模块名称
     * @param description 操作描述
     * @param success     是否成功
     * @param ipAddress   IP地址
     * @param parameters  操作参数
     */
    public static void recordOperationLog(Long userId, String username, String operation, 
                                        String module, String description, boolean success, 
                                        String ipAddress, Map<String, Object> parameters) {
        recordAuditLog(userId, username, operation, module, description, success, ipAddress, parameters);
    }

    /**
     * 记录数据变更日志
     * 
     * @param userId      用户ID
     * @param username    用户名
     * @param operation   操作类型
     * @param module      模块名称
     * @param entityId    实体ID
     * @param oldValue    旧值
     * @param newValue    新值
     * @param success     是否成功
     * @param ipAddress   IP地址
     */
    public static void recordDataChangeLog(Long userId, String username, String operation, 
                                         String module, Long entityId, 
                                         Map<String, Object> oldValue, Map<String, Object> newValue, 
                                         boolean success, String ipAddress) {
        Map<String, Object> parameters = new ConcurrentHashMap<>();
        parameters.put("entityId", entityId);
        parameters.put("oldValue", oldValue);
        parameters.put("newValue", newValue);

        recordAuditLog(userId, username, operation, module, 
                     operation + "操作" + (success ? "成功" : "失败"), success, 
                     ipAddress, parameters);
    }

    // ==================== 日志处理 ====================

    /**
     * 处理审计日志
     * 这里可以根据实际情况存储到数据库、Elasticsearch 或其他日志系统
     * 
     * @param logContent 日志内容
     */
    private static void logAuditLog(Map<String, Object> logContent) {
        // 目前使用 SLF4J 记录到日志文件
        // 实际项目中可以扩展为存储到数据库或 Elasticsearch
        String level = (String) logContent.get("level");
        String username = (String) logContent.get("username");
        String operation = (String) logContent.get("operation");
        String module = (String) logContent.get("module");
        String description = (String) logContent.get("description");
        boolean success = (boolean) logContent.get("success");
        String ipAddress = (String) logContent.get("ipAddress");

        String logMessage = String.format("[AUDIT] %s | %s | %s | %s | %s | %s | %s",
                level, username, operation, module, description, success ? "SUCCESS" : "FAILED", ipAddress);

        switch (level) {
            case LEVEL_INFO:
                log.info(logMessage);
                break;
            case LEVEL_WARN:
                log.warn(logMessage);
                break;
            case LEVEL_ERROR:
                log.error(logMessage);
                break;
            case LEVEL_DEBUG:
                log.debug(logMessage);
                break;
            default:
                log.info(logMessage);
        }

        // 这里可以添加异步存储到数据库的逻辑
        // 例如使用消息队列或异步线程池
    }

    // ==================== 工具方法 ====================

    /**
     * 生成操作描述
     * 
     * @param operation 操作类型
     * @param module    模块名称
     * @param target    操作目标
     * @return 操作描述
     */
    public static String generateDescription(String operation, String module, String target) {
        StringBuilder sb = new StringBuilder();
        sb.append(module);

        switch (operation) {
            case OPERATION_CREATE:
                sb.append("创建");
                break;
            case OPERATION_UPDATE:
                sb.append("更新");
                break;
            case OPERATION_DELETE:
                sb.append("删除");
                break;
            case OPERATION_QUERY:
                sb.append("查询");
                break;
            case OPERATION_IMPORT:
                sb.append("导入");
                break;
            case OPERATION_EXPORT:
                sb.append("导出");
                break;
            default:
                sb.append("操作");
        }

        if (target != null) {
            sb.append(target);
        }

        return sb.toString();
    }

    /**
     * 检查审计日志配置是否启用
     * 
     * @return 是否启用
     */
    public static boolean isAuditLogEnabled() {
        // 这里可以根据配置判断是否启用审计日志
        // 暂时返回 true，表示启用
        return true;
    }
}
