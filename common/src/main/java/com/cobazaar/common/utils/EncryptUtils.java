package com.cobazaar.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.cobazaar.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * 加密解密工具类
 * 基于Hutool实现常用的加密解密功能
 */
@Slf4j
public final class EncryptUtils {

    /**
     * MD5加密
     * 
     * @param content 待加密内容
     * @return MD5加密结果
     */
    public static String md5(String content) {
        if (StrUtil.isBlank(content)) {
            return null;
        }
        return SecureUtil.md5(content);
    }

    /**
     * SHA256加密
     * 
     * @param content 待加密内容
     * @return SHA256加密结果
     */
    public static String sha256(String content) {
        if (StrUtil.isBlank(content)) {
            return null;
        }
        return SecureUtil.sha256(content);
    }

    /**
     * AES加密
     * 
     * @param content 待加密内容
     * @param key 密钥（16位）
     * @return 加密结果（十六进制字符串）
     */
    public static String aesEncrypt(String content, String key) {
        if (StrUtil.isBlank(content) || StrUtil.isBlank(key)) {
            throw new IllegalArgumentException("内容和密钥不能为空");
        }
        if (key.length() != 16) {
            throw new IllegalArgumentException("AES密钥长度必须为16位");
        }
        
        try {
            cn.hutool.crypto.symmetric.AES aes = SecureUtil.aes(key.getBytes());
            String encrypted = aes.encryptHex(content);
            log.debug("AES加密成功");
            return encrypted;
        } catch (Exception e) {
            log.error("AES加密失败", e);
            throw new ServiceException("AES加密失败", e);
        }
    }

    /**
     * AES解密
     * 
     * @param encryptedContent 加密内容（十六进制字符串）
     * @param key 密钥（16位）
     * @return 解密结果
     */
    public static String aesDecrypt(String encryptedContent, String key) {
        if (StrUtil.isBlank(encryptedContent) || StrUtil.isBlank(key)) {
            throw new IllegalArgumentException("加密内容和密钥不能为空");
        }
        if (key.length() != 16) {
            throw new IllegalArgumentException("AES密钥长度必须为16位");
        }
        
        try {
            cn.hutool.crypto.symmetric.AES aes = SecureUtil.aes(key.getBytes());
            String decrypted = aes.decryptStr(encryptedContent);
            log.debug("AES解密成功");
            return decrypted;
        } catch (Exception e) {
            log.error("AES解密失败", e);
            throw new ServiceException("AES解密失败", e);
        }
    }

    /**
     * Base64编码
     * 
     * @param content 待编码内容
     * @return Base64编码结果
     */
    public static String base64Encode(String content) {
        if (StrUtil.isBlank(content)) {
            return null;
        }
        return java.util.Base64.getEncoder().encodeToString(content.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    /**
     * Base64解码
     * 
     * @param encodedContent Base64编码内容
     * @return 解码结果
     */
    public static String base64Decode(String encodedContent) {
        if (StrUtil.isBlank(encodedContent)) {
            return null;
        }
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(encodedContent);
        return new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    /**
     * 验证密码强度
     * 
     * @param password 密码
     * @return 是否满足强度要求（至少8位，包含数字、字母、特殊字符）
     */
    public static boolean validatePasswordStrength(String password) {
        if (StrUtil.isBlank(password)) {
            return false;
        }
        
        // 长度至少8位
        if (password.length() < 8) {
            return false;
        }
        
        // 包含数字
        boolean hasDigit = password.matches(".*\\d.*");
        // 包含字母
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        // 包含特殊字符
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        return hasDigit && hasLetter && hasSpecialChar;
    }
}