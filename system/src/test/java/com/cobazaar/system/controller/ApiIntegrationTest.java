package com.cobazaar.system.controller;

import com.cobazaar.common.result.Result;
import com.cobazaar.system.SystemApplication;
import com.cobazaar.system.config.TestRedisConfig;
import com.cobazaar.system.dto.LoginDTO;
import com.cobazaar.system.dto.RegisterDTO;
import com.cobazaar.system.dto.UpdateUserDTO;
import com.cobazaar.system.vo.LoginVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * API 集成测试类
 * 测试所有 HTTP 接口
 *
 * @author cobazaar
 */
@SpringBootTest(classes = {SystemApplication.class, TestRedisConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;
    private Long testUserId;

    @BeforeEach
    public void setUp() {
        // 登录获取访问令牌
        login();
    }

    /**
     * 登录获取访问令牌
     */
    private void login() {
        // 构建登录请求
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("admin123");

        // 构建请求实体
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);

        // 发送登录请求 - 使用ParameterizedTypeReference指定泛型类型
        ResponseEntity<Result<LoginVO>> response = restTemplate.exchange(
                "/auth/login",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Result<LoginVO>>() {}
        );

        // 验证登录结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        // 直接获取LoginVO对象
        LoginVO loginVO = response.getBody().getData();
        accessToken = loginVO.getAccessToken();
        assertNotNull(accessToken);
    }

    /**
     * 创建 HTTP 请求头
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        return headers;
    }

    /**
     * 测试登录接口
     */
    @Test
    public void testLogin() {
        // 构建登录请求
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("admin123");

        // 构建请求实体
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);

        // 发送登录请求 - 使用ParameterizedTypeReference指定泛型类型
        ResponseEntity<Result<LoginVO>> response = restTemplate.exchange(
                "/auth/login",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Result<LoginVO>>() {}
        );

        // 验证登录结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        // 直接获取LoginVO对象
        LoginVO loginVO = response.getBody().getData();
        assertNotNull(loginVO.getAccessToken());
        assertNotNull(loginVO.getRefreshToken());
        assertNotNull(loginVO.getUser());
    }

    /**
     * 测试获取用户列表接口
     */
    @Test
    public void testGetUserList() {
        // 创建请求头
        HttpHeaders headers = createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 发送请求 - 移除/system前缀
        ResponseEntity<String> response = restTemplate.exchange(
                "/user/page",
                HttpMethod.GET,
                entity,
                String.class
        );

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * 测试获取用户详情接口
     */
    @Test
    public void testGetUserById() {
        // 创建请求头
        HttpHeaders headers = createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 发送请求 - 移除/system前缀
        ResponseEntity<String> response = restTemplate.exchange(
                "/user/1",
                HttpMethod.GET,
                entity,
                String.class
        );

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * 测试新增用户接口
     */
    @Test
    public void testAddUser() {
        // 构建新增用户请求
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser" + System.currentTimeMillis());
        registerDTO.setPassword("123456");
        registerDTO.setConfirmPassword("123456");
        registerDTO.setNickname("测试用户");
        registerDTO.setPhone("13800138000");
        registerDTO.setEmail("testuser@demo.com");

        // 创建请求头
        HttpHeaders headers = createHeaders();
        HttpEntity<RegisterDTO> entity = new HttpEntity<>(registerDTO, headers);

        // 发送请求 - 移除/system前缀
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/user",
                entity,
                String.class
        );

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * 测试修改用户接口
     */
    @Test
    public void testUpdateUser() {
        // 构建修改用户请求
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(1L);
        updateUserDTO.setUsername("admin");
        updateUserDTO.setNickname("管理员");
        updateUserDTO.setRealName("系统管理员");
        updateUserDTO.setPhone("13800138000");
        updateUserDTO.setEmail("admin@demo.com");
        updateUserDTO.setStatus(1);

        // 创建请求头
        HttpHeaders headers = createHeaders();
        HttpEntity<UpdateUserDTO> entity = new HttpEntity<>(updateUserDTO, headers);

        // 发送请求 - 移除/system前缀
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/user/update",
                entity,
                String.class
        );

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * 测试获取菜单列表接口
     */
    @Test
    public void testGetMenuTree() {
        // 创建请求头
        HttpHeaders headers = createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 发送请求 - 移除/system前缀
        ResponseEntity<String> response = restTemplate.exchange(
                "/menu/tree",
                HttpMethod.GET,
                entity,
                String.class
        );

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * 测试获取当前用户信息接口
     */
    @Test
    public void testGetCurrentUser() {
        // 创建请求头
        HttpHeaders headers = createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 发送请求 - 移除/system前缀
        ResponseEntity<String> response = restTemplate.exchange(
                "/auth/me",
                HttpMethod.POST,
                entity,
                String.class
        );

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * 测试刷新令牌接口
     */
    @Test
    public void testRefreshToken() {
        // 先登录获取刷新令牌
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("admin123");

        // 构建请求实体
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);

        // 发送登录请求 - 使用ParameterizedTypeReference指定泛型类型
        ResponseEntity<Result<LoginVO>> loginResponse = restTemplate.exchange(
                "/auth/login",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Result<LoginVO>>() {}
        );

        // 直接获取LoginVO对象
        LoginVO loginVO = loginResponse.getBody().getData();
        String refreshToken = loginVO.getRefreshToken();
        assertNotNull(refreshToken);

        // 创建请求头
        HttpHeaders refreshHeaders = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(refreshToken, refreshHeaders);

        // 发送请求 - 移除/system前缀
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/auth/refresh",
                entity,
                String.class
        );

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * 测试登出接口
     */
    @Test
    public void testLogout() {
        // 创建请求头
        HttpHeaders headers = createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 发送请求 - 移除/system前缀
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/auth/logout",
                entity,
                String.class
        );

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * 测试修改密码接口
     */
    @Test
    public void testChangePassword() {
        // 构建修改密码请求
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(1L);
        updateUserDTO.setUsername("admin");
        updateUserDTO.setPassword("123456");

        // 创建请求头
        HttpHeaders headers = createHeaders();
        HttpEntity<UpdateUserDTO> entity = new HttpEntity<>(updateUserDTO, headers);

        // 发送请求 - 移除/system前缀
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/user/changePassword",
                entity,
                String.class
        );

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
