package com.cobazaar.system.controller;

import com.cobazaar.common.result.Result;
import com.cobazaar.common.vo.ContactInfo;
import com.cobazaar.system.entity.User;
import com.cobazaar.system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试敏感数据控制器
 * 处理无需认证的敏感数据测试请求
 */
@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestSensitiveController {
    
    private final UserMapper userMapper;
    
    /**
     * 测试获取用户完整联系信息
     * 无需认证，直接从数据库查询数据
     * @param userId 目标用户ID
     * @return 联系信息
     */
    @GetMapping("/sensitive/contact")
    public Result<ContactInfo> getContactInfo(
            @RequestParam("userId") Long userId) {
        
        try {
            log.info("测试模式：获取用户 {} 的完整联系信息", userId);
            
            // 从数据库查询用户信息
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 构建返回对象
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setUserId(userId);
            contactInfo.setUsername(user.getUsername());
            contactInfo.setNickname(user.getNickname());
            contactInfo.setPhone(user.getPhone());
            contactInfo.setEmail(user.getEmail());
            
            return Result.success(contactInfo);
        } catch (Exception e) {
            log.error("测试获取用户联系信息失败: {}", e.getMessage(), e);
            return Result.error("测试获取用户联系信息失败: " + e.getMessage());
        }
    }
}
