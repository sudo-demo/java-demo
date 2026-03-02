package com.cobazaar.common.exception;

import com.cobazaar.common.enums.ResponseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 验证异常类
 * 用于处理参数验证相关的异常，如：参数为空、参数格式错误、参数值无效等
 */
public class ValidateException extends BizException {

    /**
     * 构造函数
     */
    public ValidateException(String message) {
        super(ResponseEnum.PARAM_ERROR.getCode(), message);
    }

    /**
     * 构造函数
     */
    public ValidateException(Integer code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     */
    public ValidateException(ResponseEnum responseEnum) {
        super(responseEnum);
    }

    /**
     * 构造函数
     */
    public ValidateException(ResponseEnum responseEnum, String message) {
        super(responseEnum, message);
    }

    /**
     * 构造函数
     */
    public ValidateException(String message, Throwable cause) {
        super(ResponseEnum.PARAM_ERROR.getCode(), message);
        initCause(cause);
    }
}