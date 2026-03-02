package com.cobazaar.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cobazaar.system.entity.ThirdPartyAuth;

import java.util.List;

/**
 * 第三方认证服务接口
 * 提供第三方认证相关的业务逻辑操作方法
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
public interface ThirdPartyAuthService extends IService<ThirdPartyAuth> {

    /**
     * 绑定第三方账号
     *
     * @param bindDTO 第三方认证绑定信息
     * @return 是否成功
     */
    boolean bindThirdPartyAuth(com.cobazaar.system.dto.ThirdPartyAuthBindDTO bindDTO);

    /**
     * 解绑第三方账号
     *
     * @param id 第三方认证ID
     */
    void unbindThirdPartyAuth(Long id);

    /**
     * 根据平台类型和平台用户ID查询认证信息
     *
     * @param platformType 平台类型
     * @param platformUserId 平台用户ID
     * @return 第三方认证信息
     */
    ThirdPartyAuth getByPlatformUserId(Integer platformType, String platformUserId);

    /**
     * 根据用户ID获取绑定的第三方账号列表
     *
     * @param userId 用户ID
     * @return 第三方认证列表
     */
    List<com.cobazaar.system.vo.ThirdPartyAuthVO> getByUserId(Long userId);

    /**
     * 刷新访问令牌
     *
     * @param id 第三方认证ID
     * @param newAccessToken 新的访问令牌
     * @param newRefreshToken 新的刷新令牌
     * @param expireTime 过期时间
     */
    void refreshToken(Long id, String newAccessToken, String newRefreshToken, java.time.LocalDateTime expireTime);

    /**
     * 检查第三方账号是否已绑定
     *
     * @param platformType 平台类型
     * @param platformUserId 平台用户ID
     * @return 是否已绑定
     */
    boolean isBound(Integer platformType, String platformUserId);
}
