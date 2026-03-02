package com.cobazaar.system.service;

import com.cobazaar.system.SystemApplication;
import com.cobazaar.system.config.TestRedisConfig;
import com.cobazaar.system.dto.LoginDTO;
import com.cobazaar.system.dto.RegisterDTO;
import com.cobazaar.system.vo.LoginVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 认证服务测试类
 * 测试AuthService中的所有方法
 *
 * @author cobazaar
 */
@SpringBootTest(classes = {SystemApplication.class, TestRedisConfig.class})
@ActiveProfiles("test")
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    private LoginDTO loginDTO;
    private RegisterDTO registerDTO;
    private String refreshToken;

    @BeforeEach
    public void setUp() {
        // 初始化登录测试数据
        loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("admin123");

        // 初始化注册测试数据
        registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser" + System.currentTimeMillis());
        registerDTO.setPassword("123456");
        registerDTO.setConfirmPassword("123456");
        registerDTO.setNickname("测试用户");
        registerDTO.setPhone("13800138000");
        registerDTO.setEmail("testuser@demo.com");
    }

    /**
     * 测试用户登录
     * 验证登录功能是否正常工作
     */
    @Test
    public void testLogin() {
        // 测试登录方法
        LoginVO loginVO = authService.login(loginDTO);
        // 验证登录结果
        assertNotNull(loginVO);
        assertNotNull(loginVO.getAccessToken());
        assertNotNull(loginVO.getRefreshToken());
        assertNotNull(loginVO.getUser());
        
        // 保存refreshToken用于后续测试
        refreshToken = loginVO.getRefreshToken();
    }

    /**
     * 测试用户登录（带验证码）
     * 验证登录功能在提供验证码时是否正常工作
     */
    @Test
    public void testLoginWithCaptcha() {
        // 设置验证码信息（这里使用模拟数据，实际测试时应该调用captcha服务生成）
        loginDTO.setCaptcha("1234");
        loginDTO.setCaptchaId("test-captcha-id");
        
        // 测试登录方法
        LoginVO loginVO = authService.login(loginDTO);
        // 验证登录结果
        assertNotNull(loginVO);
        assertNotNull(loginVO.getAccessToken());
        assertNotNull(loginVO.getRefreshToken());
        assertNotNull(loginVO.getUser());
    }

    /**
     * 测试用户注册
     * 验证注册功能是否正常工作
     */
    @Test
    public void testRegister() {
        // 测试注册方法
        LoginVO loginVO = authService.register(registerDTO);
        // 验证注册结果
        assertNotNull(loginVO);
        assertNotNull(loginVO.getAccessToken());
        assertNotNull(loginVO.getRefreshToken());
        assertNotNull(loginVO.getUser());
    }

    /**
     * 测试用户注册（带验证码）
     * 验证注册功能在提供验证码时是否正常工作
     */
    @Test
    public void testRegisterWithCaptcha() {
        // 设置验证码信息
        registerDTO.setCaptcha("1234");
        registerDTO.setCaptchaId("test-captcha-id");
        
        // 测试注册方法
        LoginVO loginVO = authService.register(registerDTO);
        // 验证注册结果
        assertNotNull(loginVO);
        assertNotNull(loginVO.getAccessToken());
        assertNotNull(loginVO.getRefreshToken());
        assertNotNull(loginVO.getUser());
    }

    /**
     * 测试刷新令牌
     * 验证刷新令牌功能是否正常工作
     */
    @Test
    public void testRefreshToken() {
        // 先登录获取refreshToken
        if (refreshToken == null) {
            LoginVO loginVO = authService.login(loginDTO);
            refreshToken = loginVO.getRefreshToken();
        }
        
        // 测试刷新令牌方法
        LoginVO loginVO = authService.refreshToken(refreshToken);
        // 验证刷新结果
        assertNotNull(loginVO);
        assertNotNull(loginVO.getAccessToken());
        assertNotNull(loginVO.getRefreshToken());
        assertNotNull(loginVO.getUser());
    }

    /**
     * 测试获取当前用户信息
     * 验证获取当前用户信息功能是否正常工作
     */
    @Test
    public void testGetCurrentUser() {
        // 先登录
        authService.login(loginDTO);
        
        // 测试获取当前用户信息方法
        Object userInfo = authService.getCurrentUser();
        // 验证结果
        assertNotNull(userInfo);
    }

    /**
     * 测试用户登出
     * 验证登出功能是否正常工作
     */
    @Test
    public void testLogout() {
        // 先登录
        authService.login(loginDTO);
        
        // 测试登出方法
        authService.logout();
        // 登出方法没有返回值，只要不抛异常就算成功
    }

    /**
     * 测试修改密码
     * 验证修改密码功能是否正常工作
     */
    @Test
    public void testChangePassword() {
        // 先登录
        authService.login(loginDTO);
        
        // 测试修改密码方法
        boolean result = authService.changePassword("admin123", "123456");
        // 验证修改结果
        assertTrue(result);
        
        // 改回原密码，方便后续测试
        authService.changePassword("123456", "admin123");
    }
}

