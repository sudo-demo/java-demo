package com.cobazaar.common.vo;

import lombok.Data;

/**
 * 联系信息VO
 * 用于返回用户的完整联系信息（手机号、邮箱等）
 */
@Data
public class ContactInfo {
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
}
