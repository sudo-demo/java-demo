package com.cobazaar.common.exception;

import com.cobazaar.common.enums.ResponseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源异常类
 * 用于资源相关的异常，如资源不存在、资源访问失败等
 * @author huanghongjia
 */
public class ResourceException extends BizException {

    /**
     * 构造函数
     * @param message 错误信息
     */
    public ResourceException(String message) {
        super(ResponseEnum.NOT_FOUND, message);
    }

    /**
     * 构造函数
     * @param responseEnum 响应枚举
     */
    public ResourceException(ResponseEnum responseEnum) {
        super(responseEnum);
    }

    /**
     * 构造函数
     * @param responseEnum 响应枚举
     * @param message 错误信息
     */
    public ResourceException(ResponseEnum responseEnum, String message) {
        super(responseEnum, message);
    }

    /**
     * 构造函数
     * @param message 错误信息
     * @param cause 异常原因
     */
    public ResourceException(String message, Throwable cause) {
        super(message, cause);
        this.setCode(ResponseEnum.NOT_FOUND.getCode());
    }
}
