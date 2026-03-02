package com.cobazaar.system.service;

import com.cobazaar.common.vo.ContactInfo;
import com.cobazaar.system.entity.User;
import com.cobazaar.system.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 敏感数据服务
 * 处理敏感数据的获取和权限校验
 */
@Slf4j
@Service
public class SensitiveDataService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private UserService userService;
    
    /**
     * 验证用户是否有权查看目标用户的联系信息
     * @param currentUserId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 是否有权限
     */
    public boolean validateContactPermission(Long currentUserId, Long targetUserId) {
        // 1. 检查当前用户是否已登录
        if (currentUserId == null) {
            return false;
        }
        
        // 2. 检查是否为系统管理员（管理员可查看所有）
        try {
            if (userService.isAdmin(currentUserId)) {
                return true;
            }
        } catch (Exception e) {
            // 调用isAdmin失败，继续后续检查
            log.debug("检查管理员权限失败: {}", e.getMessage());
        }
        
        // 3. 检查是否为目标用户本人（用户可查看自己）
        if (currentUserId.equals(targetUserId)) {
            return true;
        }
        
        // 4. 检查是否有业务关系（如合作订单、好友关系等）
        // 此处可根据具体业务需求扩展，例如：
        // - 检查是否为同一部门
        // - 检查是否有历史交易记录
        // - 检查是否为好友关系
        // 暂时默认允许普通用户查看其他用户的联系信息
        // TODO: 根据业务需求调整权限校验逻辑
        
        return true;
    }
    
    /**
     * 检查接口调用频率
     * @param userId 用户ID
     * @return 是否允许调用
     */
    public boolean checkRateLimit(Long userId) {
        String key = "rate_limit:sensitive:contact:" + userId;
        Integer count = (Integer) redisTemplate.opsForValue().get(key);
        
        if (count == null) {
            // 首次调用，设置计数为1，有效期1分钟
            redisTemplate.opsForValue().set(key, 1, 1, TimeUnit.MINUTES);
            return true;
        } else if (count < 5) {
            // 调用次数小于限制，增加计数
            redisTemplate.opsForValue().increment(key);
            return true;
        } else {
            // 超过限制
            log.warn("用户 {} 查看敏感信息频率过高，已被限制", userId);
            return false;
        }
    }
    
    /**
     * 模拟用户数据
     */
    private static final Map<Long, Map<String, String>> MOCK_USER_DATA = new ConcurrentHashMap<>();
    
    static {
        // 初始化模拟数据
        Map<String, String> adminUser = new HashMap<>();
        adminUser.put("username", "admin");
        adminUser.put("nickname", "管理员");
        adminUser.put("phone", "13812348000");
        adminUser.put("email", "admin@demo.com");
        MOCK_USER_DATA.put(1L, adminUser);
        
        Map<String, String> testUser = new HashMap<>();
        testUser.put("username", "test");
        testUser.put("nickname", "测试用户");
        testUser.put("phone", "13912349000");
        testUser.put("email", "test@demo.com");
        MOCK_USER_DATA.put(2L, testUser);
    }
    
    /**
     * 获取用户完整联系信息
     * @param currentUserId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 联系信息
     */
    public ContactInfo getContactInfo(Long currentUserId, Long targetUserId) {
        // 1. 检查当前用户是否已登录
        if (currentUserId == null) {
            throw new com.cobazaar.common.exception.ServiceException("用户未登录，无法查看敏感信息");
        }
        
        // 2. 检查调用频率
        if (!checkRateLimit(currentUserId)) {
            throw new com.cobazaar.common.exception.ServiceException("查看敏感信息频率过高，请稍后再试");
        }
        
        // 3. 验证权限
        if (!validateContactPermission(currentUserId, targetUserId)) {
            throw new com.cobazaar.common.exception.ServiceException("无权限查看该用户的联系信息");
        }
        
        // 4. 从数据库查询用户信息
        User user = userMapper.selectById(targetUserId);
        if (user == null) {
            throw new com.cobazaar.common.exception.ServiceException("用户不存在");
        }
        
        // 5. 记录审计日志
        log.info("用户 {} 查看了用户 {} 的完整联系信息", currentUserId, targetUserId);
        // TODO: 实现审计日志的持久化存储
        
        // 6. 构建返回对象
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setUserId(targetUserId);
        contactInfo.setUsername(user.getUsername());
        contactInfo.setNickname(user.getNickname());
        contactInfo.setPhone(user.getPhone());  // 返回完整手机号
        contactInfo.setEmail(user.getEmail());  // 返回完整邮箱
        
        return contactInfo;
    }
}
