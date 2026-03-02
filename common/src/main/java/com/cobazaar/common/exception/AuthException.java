package com.cobazaar.common.exception;

import com.cobazaar.common.enums.ResponseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 认证异常类
 * 用于处理认证相关的异常，如：登录失败、令牌无效、权限不足等
 */
public class AuthException extends BizException {

    /**
     * 构造函数
     */
    public AuthException(String message) {
        super(ResponseEnum.UNAUTHORIZED.getCode(), message);
    }

    /**
     * 构造函数
     */
    public AuthException(Integer code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     */
    public AuthException(ResponseEnum responseEnum) {
        super(responseEnum);
    }

    /**
     * 构造函数
     */
    public AuthException(ResponseEnum responseEnum, String message) {
        super(responseEnum, message);
    }

    /**
     * 构造函数
     */
    public AuthException(String message, Throwable cause) {
        super(ResponseEnum.UNAUTHORIZED.getCode(), message);
        initCause(cause);
    }
}