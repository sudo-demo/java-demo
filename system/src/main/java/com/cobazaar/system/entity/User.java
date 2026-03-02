package com.cobazaar.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author cobazaar
 */
@Schema(name = "User", description = "用户实体类")
@Data
@TableName("sys_user")
public class User {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    
    /**
     * 用户名
     */
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    
    /**
     * 密码
     */
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    
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
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime updateTime;
    
    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long createBy;
    
    /**
     * 更新人ID
     */
    @Schema(description = "更新人ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long updateBy;
    
    /**
     * 删除标识（0-正常，1-删除）
     */
    @Schema(description = "删除标识（0-正常，1-删除）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer delFlag;
    
    /**
     * 版本号（乐观锁）
     */
    @Schema(description = "版本号（乐观锁）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer version;
}