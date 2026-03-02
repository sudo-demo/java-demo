package com.cobazaar.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态枚举
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    /**
     * 成功
     */
    SUCCESS(0, "success"),

    /**
     * 失败
     */
    FAIL(1, "fail"),

    /**
     * 参数错误
     */
    PARAM_ERROR(1001, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(1002, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(1003, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(1004, "资源不存在"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(1005, "请求超时"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(1006, "服务器内部错误"),

    /**
     * 网关错误
     */
    BAD_GATEWAY(1007, "网关错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(1008, "服务不可用"),

    /**
     * 网关超时
     */
    GATEWAY_TIMEOUT(1009, "网关超时"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR(2001, "业务异常");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 描述信息
     */
    private final String message;

    /**
     * 根据状态码获取枚举
     */
    public static ResponseEnum getByCode(Integer code) {
        for (ResponseEnum responseEnum : values()) {
            if (responseEnum.getCode().equals(code)) {
                return responseEnum;
            }
        }
        return FAIL;
    }
}