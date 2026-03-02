package com.cobazaar.system.service;

import com.cobazaar.system.dto.LoginDTO;
import com.cobazaar.system.vo.LoginVO;

/**
 * 认证服务接口
 * 处理登录、登出、刷新令牌等认证相关的业务逻辑
 * @author cobazaar
 */
public interface AuthService {

    /**
     * 用户登录
     * @param loginDTO 登录请求参数
     * @return 登录响应
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 刷新令牌
     * @param refreshToken 刷新令牌
     * @return 新访问令牌
     */
    LoginVO refreshToken(String refreshToken);

    /**
     * 获取当前用户信息
     * @return 当前用户信息
     */
    Object getCurrentUser();

    /**
     * 用户注册
     * @param registerDTO 注册请求参数
     * @return 注册结果
     */
    LoginVO register(Object registerDTO);

    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    boolean changePassword(String oldPassword, String newPassword);
}
