package com.cobazaar.common.annotation;

import java.lang.annotation.*;

/**
 * 数据脱敏注解
 * 用于标记需要脱敏的字段
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataMasking {
    
    /**
     * 脱敏类型
     */
    MaskingType type();
    
    /**
     * 脱敏类型枚举
     */
    enum MaskingType {
        MOBILE,    // 手机号
        ID_CARD,   // 身份证号
        EMAIL,     // 邮箱
        BANK_CARD, // 银行卡号
        ADDRESS,   // 地址
        NAME,      // 姓名
        PASSWORD   // 密码
    }
}