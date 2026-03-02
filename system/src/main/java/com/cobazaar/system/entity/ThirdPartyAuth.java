package com.cobazaar.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 第三方认证实体类
 * 对应数据库表 sys_third_party_auth
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Schema(name = "ThirdPartyAuth", description = "第三方认证实体类")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_third_party_auth")
public class ThirdPartyAuth extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    /**
     * 第三方平台类型：1-微信 2-QQ 3-钉钉 4-GitHub 5-其他
     */
    @Schema(description = "第三方平台类型：1-微信 2-QQ 3-钉钉 4-GitHub 5-其他", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer platformType;

    /**
     * 第三方平台用户ID
     */
    @Schema(description = "第三方平台用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String platformUserId;

    /**
     * 第三方平台用户名
     */
    @Schema(description = "第三方平台用户名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platformUsername;

    /**
     * 第三方平台头像
     */
    @Schema(description = "第三方平台头像", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platformAvatar;

    /**
     * 访问令牌
     */
    @Schema(description = "访问令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accessToken;

    /**
     * 刷新令牌
     */
    @Schema(description = "刷新令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;

    /**
     * 令牌过期时间
     */
    @Schema(description = "令牌过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime tokenExpireTime;

    /**
     * 绑定时间
     */
    @Schema(description = "绑定时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime bindTime;

    /**
     * 状态：0-解绑 1-绑定
     */
    @Schema(description = "状态：0-解绑 1-绑定", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
}
