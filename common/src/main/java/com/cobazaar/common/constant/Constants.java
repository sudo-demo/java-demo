package com.cobazaar.common.constant;

/**
 * 系统常量定义
 */
public interface Constants {

    /**
     * 系统相关常量
     */
    interface System {
        /** 系统名称 */
        String SYSTEM_NAME = "cobazaar";
        /** 系统版本 */
        String SYSTEM_VERSION = "1.0.0";
        /** 默认编码 */
        String DEFAULT_CHARSET = "UTF-8";
    }

    /**
     * HTTP相关常量
     */
    interface Http {
        /** 请求头：授权 */
        String HEADER_AUTHORIZATION = "Authorization";
        /** 请求头：用户ID */
        String HEADER_USER_ID = "X-User-ID";
        /** 请求头：请求ID */
        String HEADER_REQUEST_ID = "X-Request-ID";
        /** 请求头：来源 */
        String HEADER_FROM = "from";
        /** 响应头：网关版本 */
        String HEADER_GATEWAY_VERSION = "X-Gateway-Version";
    }

    /**
     * Redis相关常量
     */
    interface Redis {
        /** Redis键前缀：用户 */
        String PREFIX_USER = "user:";
        /** Redis键前缀：角色 */
        String PREFIX_ROLE = "role:";
        /** Redis键前缀：权限 */
        String PREFIX_PERMISSION = "permission:";
        /** Redis键前缀：缓存 */
        String PREFIX_CACHE = "cache:";
        /** 默认过期时间：1小时 */
        long DEFAULT_EXPIRE = 3600L;
        /** 长时间过期：7天 */
        long LONG_EXPIRE = 604800L;
    }

    /**
     * 业务相关常量
     */
    interface Business {
        /** 默认页码 */
        int DEFAULT_PAGE_NUM = 1;
        /** 默认页大小 */
        int DEFAULT_PAGE_SIZE = 10;
        /** 最大页大小 */
        int MAX_PAGE_SIZE = 100;
    }
}