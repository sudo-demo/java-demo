package com.cobazaar.common.utils;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.cobazaar.common.config.JwtConfig;
import com.cobazaar.common.enums.TokenTypeEnum;
import com.cobazaar.common.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * 基于Hutool实现JWT令牌的生成、验证和解析功能
 * 作为Spring Bean使用，支持依赖注入
 *
 * @author cobazaar
 */
@Slf4j
@Component
public class JwtUtils {

    /**
     * JWT配置
     */
    private final JwtConfig jwtConfig;

    /**
     * 构造器注入
     * @param jwtConfig JWT配置
     */
    @Autowired
    public JwtUtils(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        log.info("JwtUtils初始化，使用配置: {}", jwtConfig);
    }

    /**
     * 生成JWT令牌
     *
     * @param payload 载荷数据
     * @param secretKey 密钥
     * @return JWT令牌
     */
    public String generateToken(Map<String, Object> payload, String secretKey, TokenTypeEnum tokenTypeEnum) {
        return generateToken(payload, secretKey, jwtConfig.getExpire(), tokenTypeEnum);
    }

    /**
     * 生成JWT令牌（指定过期时间）
     *
     * @param payload 载荷数据
     * @param secretKey 密钥
     * @param expireTime 过期时间（秒）
     * @return JWT令牌
     */
    public String generateToken(Map<String, Object> payload, String secretKey, long expireTime, TokenTypeEnum tokenTypeEnum) {
        try {
            // 设置过期时间
            long expireMillis = System.currentTimeMillis() + expireTime * 1000;
            payload.put("exp", expireMillis);
            // 设置令牌类型
            payload.put("tokenType", tokenTypeEnum.getCode());

            // 设置签发时间
            payload.put("iat", System.currentTimeMillis());

            JWTSigner signer = JWTSignerUtil.createSigner(jwtConfig.getAlgorithm(), secretKey.getBytes());
            String token = JWTUtil.createToken(payload, signer);

            log.debug("生成JWT令牌成功，过期时间: {}", new Date(expireMillis));
            return token;
        } catch (Exception e) {
            log.error("生成JWT令牌失败", e);
            throw new AuthException("生成JWT令牌失败", e);
        }
    }

    /**
     * 验证JWT令牌有效性
     *
     * @param token JWT令牌
     * @param secretKey 密钥
     * @return 是否有效
     */
    public boolean validateToken(String token, String secretKey) {
        try {
            log.debug("开始验证JWT令牌: algorithm={}, secretKey={}", jwtConfig.getAlgorithm(), secretKey != null ? "******" : "null");

            JWTSigner signer = JWTSignerUtil.createSigner(jwtConfig.getAlgorithm(), secretKey.getBytes());
            JWT jwt = JWTUtil.parseToken(token);

            boolean signatureValid = jwt.setSigner(signer).verify();
            log.debug("JWT签名验证结果: {}", signatureValid);

            // 检查是否过期
            boolean expValid = true;
            if (signatureValid) {
                Map<String, Object> payloads = jwt.getPayloads();
                Object expObj = payloads.get("exp");
                if (expObj != null) {
                    long exp = 0;
                    if (expObj instanceof Number) {
                        exp = ((Number) expObj).longValue();
                    } else if (expObj instanceof String) {
                        exp = Long.parseLong((String) expObj);
                    }
                    long currentTime = System.currentTimeMillis();
                    expValid = exp > currentTime;
                    log.debug("JWT过期时间验证结果: exp={}, currentTime={}, valid={}", exp, currentTime, expValid);
                }
            }

            boolean isValid = signatureValid && expValid;
            if (!isValid) {
                log.warn("JWT令牌验证失败或已过期: signatureValid={}, expValid={}", signatureValid, expValid);
            } else {
                log.debug("JWT令牌验证成功");
            }

            return isValid;
        } catch (Exception e) {
            log.error("JWT令牌验证异常", e);
            return false;
        }
    }

    /**
     * 解析JWT令牌获取载荷
     *
     * @param token JWT令牌
     * @param secretKey 密钥
     * @return 载荷数据
     */
    public Map<String, Object> parseToken(String token, String secretKey) {
        try {
            if (!validateToken(token, secretKey)) {
                throw new AuthException("JWT令牌无效或已过期");
            }

            JWT jwt = JWTUtil.parseToken(token);
            Map<String, Object> payloads = jwt.getPayloads();

            log.debug("解析JWT令牌成功");
            return payloads;
        } catch (Exception e) {
            log.error("解析JWT令牌失败", e);
            throw new AuthException("解析JWT令牌失败", e);
        }
    }

    /**
     * 从JWT令牌中获取指定字段值
     *
     * @param token JWT令牌
     * @param secretKey 密钥
     * @param fieldName 字段名
     * @return 字段值
     */
    public Object getFieldFromToken(String token, String secretKey, String fieldName) {
        Map<String, Object> payloads = parseToken(token, secretKey);
        return payloads.get(fieldName);
    }

    /**
     * 检查JWT令牌是否即将过期（提前10分钟）
     *
     * @param token JWT令牌
     * @param secretKey 密钥
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token, String secretKey) {
        try {
            Map<String, Object> payloads = parseToken(token, secretKey);
            Long exp = (Long) payloads.get("exp");

            if (exp != null) {
                long currentTime = System.currentTimeMillis();
                long tenMinutesInMillis = 10 * 60 * 1000L;
                return (exp - currentTime) < tenMinutesInMillis;
            }

            return false;
        } catch (Exception e) {
            log.error("检查令牌过期状态失败", e);
            return true;
        }
    }

    /**
     * 获取JWT令牌的剩余有效期（毫秒）
     *
     * @param token JWT令牌
     * @param secretKey 密钥
     * @return 剩余有效期（毫秒）
     */
    public long getTokenRemainingTime(String token, String secretKey) {
        try {
            Map<String, Object> payloads = parseToken(token, secretKey);
            Long exp = (Long) payloads.get("exp");

            if (exp != null) {
                return exp - System.currentTimeMillis();
            }

            return 0;
        } catch (Exception e) {
            log.error("获取令牌剩余时间失败", e);
            return 0;
        }
    }

    /**
     * 从JWT令牌中获取用户名
     *
     * @param token JWT令牌
     * @param secretKey 密钥
     * @return 用户名
     */
    public String getUsernameFromToken(String token, String secretKey) {
        try {
            Map<String, Object> payloads = parseToken(token, secretKey);
            Object username = payloads.get("username");
            return username != null ? username.toString() : null;
        } catch (Exception e) {
            log.error("从令牌中获取用户名失败", e);
            return null;
        }
    }

    /**
     * 验证JWT令牌有效性（简化版本，使用默认密钥）
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        // 使用配置的密钥进行验证
        return validateToken(token, jwtConfig.getSecret());
    }

    /**
     * 生成JWT令牌（简化版本，使用默认参数）
     *
     * @param username 用户名
     * @return JWT令牌
     */
    public String generateToken(String username, TokenTypeEnum tokenTypeEnum) {
        Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("username", username);
        return generateToken(payload, jwtConfig.getSecret(), jwtConfig.getExpire(), tokenTypeEnum);
    }

    /**
     * 获取JWT配置
     * @return JWT配置
     */
    public JwtConfig getJwtConfig() {
        return jwtConfig;
    }

    /**
     * 获取JWT密钥
     * @return JWT密钥
     */
    public String getJwtSecret() {
        return jwtConfig.getSecret();
    }

    /**
     * 获取JWT过期时间（秒）
     * @return JWT过期时间
     */
    public long getJwtExpire() {
        return jwtConfig.getExpire();
    }

    /**
     * 获取JWT签名算法
     * @return JWT签名算法
     */
    public String getJwtAlgorithm() {
        return jwtConfig.getAlgorithm();
    }

    /**
     * 获取JWT请求头名称
     * @return JWT请求头名称
     */
    public String getJwtHeader() {
        return jwtConfig.getHeader();
    }

    /**
     * 获取JWT请求头前缀
     * @return JWT请求头前缀
     */
    public String getJwtPrefix() {
        return jwtConfig.getPrefix();
    }

    /**
     * 获取完整的JWT请求头前缀，包含空格
     * @return 完整的JWT请求头前缀
     */
    public String getFullJwtPrefix() {
        return jwtConfig.getFullPrefix();
    }

    /**
     * 验证JWT配置是否有效
     * @return 配置是否有效
     */
    public boolean isJwtConfigValid() {
        return jwtConfig.isValid();
    }
}
