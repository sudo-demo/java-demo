package com.cobazaar.common.exception;

import com.cobazaar.common.enums.ResponseEnum;

/**
 * 服务层异常类
 * 用于服务层的通用异常，如业务逻辑错误、操作失败等
 * @author huanghongjia
 */
public class ServiceException extends BizException {

    /**
     * 构造函数
     * @param message 错误信息
     */
    public ServiceException(String message) {
        super(ResponseEnum.BUSINESS_ERROR, message);
    }

    /**
     * 构造函数
     * @param responseEnum 响应枚举
     */
    public ServiceException(ResponseEnum responseEnum) {
        super(responseEnum);
    }

    /**
     * 构造函数
     * @param responseEnum 响应枚举
     * @param message 错误信息
     */
    public ServiceException(ResponseEnum responseEnum, String message) {
        super(responseEnum, message);
    }

    /**
     * 构造函数
     * @param message 错误信息
     * @param cause 异常原因
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.setCode(ResponseEnum.BUSINESS_ERROR.getCode());
    }
}
