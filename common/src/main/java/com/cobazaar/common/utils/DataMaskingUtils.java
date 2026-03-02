package com.cobazaar.common.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据脱敏工具类
 * 用于处理敏感数据的脱敏显示
 * 
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Slf4j
public final class DataMaskingUtils {

    private DataMaskingUtils() {
        throw new UnsupportedOperationException("DataMaskingUtils is a utility class and cannot be instantiated");
    }

    /**
     * 手机号脱敏
     * 格式：138****8888
     * 
     * @param mobile 手机号
     * @return 脱敏后的手机号
     */
    public static String maskMobile(String mobile) {
        if (StrUtil.isBlank(mobile) || mobile.length() != 11) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /**
     * 身份证号脱敏
     * 格式：1101**********1234
     * 
     * @param idCard 身份证号
     * @return 脱敏后的身份证号
     */
    public static String maskIdCard(String idCard) {
        if (StrUtil.isBlank(idCard) || idCard.length() != 18) {
            return idCard;
        }
        return idCard.substring(0, 4) + "**********" + idCard.substring(14);
    }

    /**
     * 邮箱脱敏
     * 格式：t***@example.com
     * 
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String maskEmail(String email) {
        if (StrUtil.isBlank(email) || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email;
        }
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 1) {
            return "*@" + domain;
        } else if (username.length() == 2) {
            return username.substring(0, 1) + "*@" + domain;
        } else {
            return username.substring(0, 1) + "***@" + domain;
        }
    }

    /**
     * 银行卡号脱敏
     * 格式：6222 **** **** 1234
     * 
     * @param bankCard 银行卡号
     * @return 脱敏后的银行卡号
     */
    public static String maskBankCard(String bankCard) {
        if (StrUtil.isBlank(bankCard) || bankCard.length() < 10) {
            return bankCard;
        }
        int length = bankCard.length();
        String prefix = bankCard.substring(0, 4);
        String suffix = bankCard.substring(length - 4);
        return prefix + " **** **** " + suffix;
    }

    /**
     * 地址脱敏
     * 格式：北京市朝阳区********
     * 
     * @param address 地址
     * @return 脱敏后的地址
     */
    public static String maskAddress(String address) {
        if (StrUtil.isBlank(address) || address.length() < 8) {
            return address;
        }
        int length = address.length();
        if (length <= 10) {
            return address.substring(0, 6) + "******";
        } else {
            return address.substring(0, 8) + "********";
        }
    }

    /**
     * 姓名脱敏
     * 格式：张*
     * 
     * @param name 姓名
     * @return 脱敏后的姓名
     */
    public static String maskName(String name) {
        if (StrUtil.isBlank(name)) {
            return name;
        }
        int length = name.length();
        if (length == 1) {
            return name;
        } else if (length == 2) {
            return name.substring(0, 1) + "*";
        } else {
            return name.substring(0, 1) + "*" + name.substring(length - 1);
        }
    }

    /**
     * 密码脱敏（完全隐藏）
     * 格式：******
     * 
     * @param password 密码
     * @return 脱敏后的密码
     */
    public static String maskPassword(String password) {
        if (StrUtil.isBlank(password)) {
            return password;
        }
        return "******";
    }

    /**
     * 自定义脱敏
     * 
     * @param str        原始字符串
     * @param startLen   保留的开始长度
     * @param endLen     保留的结束长度
     * @param maskChar   脱敏字符
     * @param maskLength 脱敏长度
     * @return 脱敏后的字符串
     */
    public static String maskCustom(String str, int startLen, int endLen, char maskChar, int maskLength) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        int length = str.length();
        if (startLen + endLen >= length) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, startLen));
        for (int i = 0; i < maskLength; i++) {
            sb.append(maskChar);
        }
        sb.append(str.substring(length - endLen));
        return sb.toString();
    }
}
