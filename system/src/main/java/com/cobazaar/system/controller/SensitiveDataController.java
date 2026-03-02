package com.cobazaar.system.controller;

import com.cobazaar.common.result.Result;
import com.cobazaar.common.vo.ContactInfo;
import com.cobazaar.common.utils.UserContext;
import com.cobazaar.system.service.SensitiveDataService;
import com.cobazaar.system.mapper.UserMapper;
import com.cobazaar.system.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 敏感数据控制器
 * 处理敏感数据的获取请求
 */
@Slf4j
@RestController
@RequestMapping("/api/sensitive")
@RequiredArgsConstructor
public class SensitiveDataController {
    
    private final SensitiveDataService sensitiveDataService;
    
    private final UserMapper userMapper;
    
    /**
     * 获取用户完整联系信息
     * @param userId 目标用户ID
     * @return 联系信息
     */
    @GetMapping("/user/contact")
    public Result<ContactInfo> getContactInfo(
            @RequestParam("userId") Long userId) {
        
        // 获取当前用户ID
        Long currentUserId = UserContext.getUserId();
        log.info("用户 {} 请求获取用户 {} 的完整联系信息", currentUserId, userId);
            
        // 调用服务层获取联系信息
        ContactInfo contactInfo = sensitiveDataService.getContactInfo(currentUserId, userId);
            
        return Result.success(contactInfo);
    }
}
