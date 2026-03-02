package com.cobazaar.common.exception;

import com.cobazaar.common.enums.ResponseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 构造函数
     */
    public BizException(String message) {
        super(message);
        this.code = ResponseEnum.FAIL.getCode();
        this.message = message;
    }

    /**
     * 构造函数
     */
    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造函数
     */
    public BizException(ResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    /**
     * 构造函数
     */
    public BizException(ResponseEnum responseEnum, String message) {
        super(message);
        this.code = responseEnum.getCode();
        this.message = message;
    }

    /**
     * 构造函数
     */
    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResponseEnum.FAIL.getCode();
        this.message = message;
    }
}