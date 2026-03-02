package com.cobazaar.system.service;

import com.cobazaar.system.SystemApplication;
import com.cobazaar.system.entity.User;
import com.cobazaar.system.dto.UserQueryDTO;
import com.cobazaar.system.vo.UserVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试类
 * 测试UserService中的所有方法
 *
 * @author cobazaar
 */
@SpringBootTest(classes = SystemApplication.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    private User testUser;
    private Long testUserId;

    @BeforeEach
    public void setUp() {
        // 创建测试用户
        testUser = new User();
        testUser.setId(System.currentTimeMillis());
        testUser.setUsername("testuser" + System.currentTimeMillis());
        testUser.setPassword("123456");
        testUser.setNickname("测试用户");
        testUser.setPhone("13800138000");
        testUser.setEmail("testuser@demo.com");
        testUser.setStatus(1);
        
        // 保存测试用户
        userService.save(testUser);
        testUserId = testUser.getId();
    }

    /**
     * 测试根据用户名获取用户信息
     * 验证getUserByUsername方法是否正常工作
     */
    @Test
    public void testGetUserByUsername() {
        // 测试获取已存在的用户
        User user = userService.getUserByUsername(testUser.getUsername());
        assertNotNull(user);
        assertEquals(testUser.getUsername(), user.getUsername());
        
        // 测试获取不存在的用户
        User nonExistentUser = userService.getUserByUsername("non-existent-user");
        assertNull(nonExistentUser);
    }

    /**
     * 测试根据用户ID获取角色ID列表
     * 验证getUserRoleIds方法是否正常工作
     */
    @Test
    public void testGetUserRoleIds() {
        List<Long> roleIds = userService.getUserRoleIds(testUserId);
        assertNotNull(roleIds);
        // 新创建的用户应该没有角色
        assertTrue(roleIds.isEmpty());
    }

    /**
     * 测试保存用户角色关联
     * 验证saveUserRoles方法是否正常工作
     */
    @Test
    public void testSaveUserRoles() {
        List<Long> roleIds = Collections.singletonList(1L);
        boolean result = userService.saveUserRoles(testUserId, roleIds);
        assertTrue(result);
        
        // 验证角色是否保存成功
        List<Long> savedRoleIds = userService.getUserRoleIds(testUserId);
        assertNotNull(savedRoleIds);
        assertFalse(savedRoleIds.isEmpty());
        assertEquals(1, savedRoleIds.size());
        assertEquals(1L, savedRoleIds.get(0));
    }

    /**
     * 测试检查用户名是否已存在
     * 验证isUsernameExists方法是否正常工作
     */
    @Test
    public void testIsUsernameExists() {
        // 测试已存在的用户名
        boolean exists = userService.isUsernameExists(testUser.getUsername(), null);
        assertTrue(exists);
        
        // 测试已存在的用户名但排除当前用户
        boolean existsExclude = userService.isUsernameExists(testUser.getUsername(), testUserId);
        assertFalse(existsExclude);
        
        // 测试不存在的用户名
        boolean notExists = userService.isUsernameExists("non-existent-username", null);
        assertFalse(notExists);
    }

    /**
     * 测试检查手机号是否已存在
     * 验证isPhoneExists方法是否正常工作
     */
    @Test
    public void testIsPhoneExists() {
        // 测试已存在的手机号
        boolean exists = userService.isPhoneExists(testUser.getPhone(), null);
        assertTrue(exists);
        
        // 测试已存在的手机号但排除当前用户
        boolean existsExclude = userService.isPhoneExists(testUser.getPhone(), testUserId);
        assertFalse(existsExclude);
        
        // 测试不存在的手机号
        boolean notExists = userService.isPhoneExists("13900139000", null);
        assertFalse(notExists);
    }

    /**
     * 测试检查邮箱是否已存在
     * 验证isEmailExists方法是否正常工作
     */
    @Test
    public void testIsEmailExists() {
        // 测试已存在的邮箱
        boolean exists = userService.isEmailExists(testUser.getEmail(), null);
        assertTrue(exists);
        
        // 测试已存在的邮箱但排除当前用户
        boolean existsExclude = userService.isEmailExists(testUser.getEmail(), testUserId);
        assertFalse(existsExclude);
        
        // 测试不存在的邮箱
        boolean notExists = userService.isEmailExists("newuser@demo.com", null);
        assertFalse(notExists);
    }

    /**
     * 测试重置用户密码
     * 验证resetPassword方法是否正常工作
     */
    @Test
    public void testResetPassword() {
        boolean result = userService.resetPassword(testUserId, "newpassword123");
        assertTrue(result);
    }

    /**
     * 测试修改用户密码
     * 验证changePassword方法是否正常工作
     */
    @Test
    public void testChangePassword() {
        // 测试修改密码
        boolean result = userService.changePassword(testUserId, "123456", "newpassword123");
        assertTrue(result);
        
        // 测试使用错误的旧密码
        boolean wrongPasswordResult = userService.changePassword(testUserId, "wrongpassword", "newpassword123");
        assertFalse(wrongPasswordResult);
    }

    /**
     * 测试更新用户最后登录时间
     * 验证updateLastLoginTime方法是否正常工作
     */
    @Test
    public void testUpdateLastLoginTime() {
        boolean result = userService.updateLastLoginTime(testUserId);
        assertTrue(result);
    }

    /**
     * 测试分页查询用户列表
     * 验证getUserPage方法是否正常工作
     */
    @Test
    public void testGetUserPage() {
        UserQueryDTO userQueryDTO = new UserQueryDTO();
        userQueryDTO.setPage(1);
        userQueryDTO.setPageSize(10);
        
        Page<UserVO> page = userService.getUserPage(userQueryDTO);
        assertNotNull(page);
        assertNotNull(page.getRecords());
    }

    /**
     * 测试将User实体转换为UserVO
     * 验证convertToVO方法是否正常工作
     */
    @Test
    public void testConvertToVO() {
        UserVO userVO = userService.convertToVO(testUser);
        assertNotNull(userVO);
        assertEquals(testUser.getId(), userVO.getId());
        assertEquals(testUser.getUsername(), userVO.getUsername());
        assertEquals(testUser.getNickname(), userVO.getNickname());
        assertEquals(testUser.getPhone(), userVO.getPhone());
        assertEquals(testUser.getEmail(), userVO.getEmail());
    }

    /**
     * 测试将User实体列表转换为UserVO列表
     * 验证convertToVOList方法是否正常工作
     */
    @Test
    public void testConvertToVOList() {
        List<User> users = Collections.singletonList(testUser);
        List<UserVO> userVOs = userService.convertToVOList(users);
        assertNotNull(userVOs);
        assertEquals(1, userVOs.size());
        assertEquals(testUser.getId(), userVOs.get(0).getId());
    }

    /**
     * 测试用户保存和删除
     * 验证IService中的save和removeById方法是否正常工作
     */
    @Test
    public void testSaveAndRemoveUser() {
        // 测试保存用户
        User newUser = new User();
        newUser.setId(System.currentTimeMillis());
        newUser.setUsername("newtestuser" + System.currentTimeMillis());
        newUser.setPassword("123456");
        newUser.setNickname("新测试用户");
        boolean saveResult = userService.save(newUser);
        assertTrue(saveResult);
        
        // 测试删除用户
        boolean removeResult = userService.removeById(newUser.getId());
        assertTrue(removeResult);
        
        // 验证用户是否删除成功
        User removedUser = userService.getById(newUser.getId());
        assertNull(removedUser);
    }
}
