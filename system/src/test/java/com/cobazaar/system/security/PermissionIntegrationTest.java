package com.cobazaar.system.security;

import com.cobazaar.common.result.Result;
import com.cobazaar.system.SystemApplication;
import com.cobazaar.system.config.TestRedisConfig;
import com.cobazaar.system.dto.LoginDTO;
import com.cobazaar.system.vo.LoginVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 权限校验集成测试
 * 验证 @PreAuthorize 注解是否生效
 *
 * @author cobazaar
 */
@SpringBootTest(classes = {SystemApplication.class, TestRedisConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PermissionIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * 测试管理员（有权限）访问用户分页接口
     */
    @Test
    public void testAdminAccessUserPage() {
        String token = login("admin", "admin123");
        assertNotNull(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/user/page",
                HttpMethod.GET,
                entity,
                String.class
        );

        // 管理员应该有 system:user:list 权限，所以应该返回 200
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * 测试普通用户（无权限）访问用户分页接口
     * 注意：这里假设 test 用户没有 system:user:list 权限
     */
    @Test
    public void testUserAccessUserPageDenied() {
        String token = login("test", "admin123");
        assertNotNull(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/user/page",
                HttpMethod.GET,
                entity,
                String.class
        );

        // 普通用户应该没有权限，安全框架或 CommonAccessDeniedHandler 应该拦截并返回 403 (虽然状态码可能是 200 但业务码是 403)
        // 根据 CommonAccessDeniedHandler 实现，HttpStatus 是 OK，但内容是 Result.fail(403)
        assertTrue(response.getBody().contains("\"code\":403") || response.getStatusCode() == HttpStatus.FORBIDDEN);
    }

    /**
     * 测试管理员访问部门树接口
     */
    @Test
    public void testAdminAccessDeptTree() {
        String token = login("admin", "admin123");
        assertNotNull(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/dept/tree",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * 测试管理员访问登录日志分页接口
     */
    @Test
    public void testAdminAccessLoginLogPage() {
        String token = login("admin", "admin123");
        assertNotNull(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/login-log/page",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * 登录并返回 token
     */
    private String login(String username, String password) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);

        ResponseEntity<Result<LoginVO>> response = restTemplate.exchange(
                "/auth/login",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Result<LoginVO>>() {}
        );

        if (response.getBody() != null && response.getBody().getData() != null) {
            return response.getBody().getData().getAccessToken();
        }
        return null;
    }
}
