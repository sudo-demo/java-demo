package com.cobazaar.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户VO
 * 用于返回用户信息
 *
 * @author cobazaar
 */
@Schema(name = "UserVO", description = "用户信息VO")
@Data
public class UserVO {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    
    /**
     * 用户名
     */
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    
    /**
     * 昵称
     */
    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String nickname;
    
    /**
     * 姓名
     */
    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String realName;
    
    /**
     * 手机号
     */
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String phone;
    
    /**
     * 邮箱
     */
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String email;
    
    /**
     * 头像
     */
    @Schema(description = "头像", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String avatar;
    
    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
    
    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime lastLoginTime;
    
    /**
     * 创建时间
     */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;
}
