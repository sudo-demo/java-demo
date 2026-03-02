package com.cobazaar.common.enums;

import lombok.Getter;

/**
 * Redis模块前缀枚举类
 * 定义不同模块的Redis key前缀，用于统一管理和区分不同模块的缓存
 *
 * @author cobazaar
 */
public enum RedisModulePrefix {

    /**
     * 登录模块前缀
     */
    LOGIN("login:"),

    /**
     * 用户模块前缀
     */
    USER("user:"),

    /**
     * 权限模块前缀
     */
    PERMISSION("permission:"),

    /**
     * 角色模块前缀
     */
    ROLE("role:"),

    /**
     * 菜单模块前缀
     */
    MENU("menu:"),

    /**
     * 缓存模块前缀
     */
    CACHE("cache:"),

    /**
     * 验证码模块前缀
     */
    CAPTCHA("captcha:"),

    /**
     * 令牌模块前缀
     */
    TOKEN("token:"),

    /**
     * 令牌黑名单前缀
     */
    TOKEN_BLACKLIST("token:blacklist:"),

    /**
     * 系统模块前缀
     */
    SYSTEM("system:"),

    /**
     * 业务模块前缀
     */
    BUSINESS("business:");

    @Getter
    private final String prefix;

    /**
     * 构造函数
     *
     * @param prefix 模块前缀
     */
    RedisModulePrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 为指定的key添加模块前缀
     *
     * @param key 原始key
     * @return 添加模块前缀后的key
     */
    public String addPrefix(String key) {
        if (key == null || key.isEmpty()) {
            return prefix;
        }
        return prefix + key;
    }
}
