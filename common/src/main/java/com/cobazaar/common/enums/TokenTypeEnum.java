package com.cobazaar.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Token类型枚举类
 *
 * @author YourName
 */
@Getter
@AllArgsConstructor
public enum TokenTypeEnum {

    /**
     * 内置登录
     */
    BUILT_IN(0, "内置登录"),

    /**
     * 统一认证
     */
    UNIFIED_AUTH(1, "统一认证"),

    /**
     * 微信登录
     */
    WECHAT_LOGIN(2, "微信登录");

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
     *
     * @param code 状态码
     * @return 对应的枚举实例，若未找到则返回默认值BUILT_IN
     */
    public static TokenTypeEnum getByCode(Integer code) {
        for (TokenTypeEnum tokenType : values()) {
            if (tokenType.getCode().equals(code)) {
                return tokenType;
            }
        }
        return BUILT_IN;
    }
}
