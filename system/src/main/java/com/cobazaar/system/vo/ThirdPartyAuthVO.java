package com.cobazaar.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 第三方认证VO
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-14
 */
@Data
@Schema(name = "ThirdPartyAuthVO", description = "第三方认证视图对象")
public class ThirdPartyAuthVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "第三方平台类型：1-微信 2-QQ 3-钉钉 4-GitHub 5-其他")
    private Integer platformType;

    @Schema(description = "第三方平台用户ID")
    private String platformUserId;

    @Schema(description = "第三方平台用户名")
    private String platformUsername;

    @Schema(description = "第三方平台头像")
    private String platformAvatar;

    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;

    @Schema(description = "状态：0-解绑 1-绑定")
    private Integer status;
}
