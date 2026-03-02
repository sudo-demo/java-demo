package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 第三方认证绑定DTO
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-14
 */
@Data
@Schema(name = "ThirdPartyAuthBindDTO", description = "第三方认证绑定数据传输对象")
public class ThirdPartyAuthBindDTO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "第三方平台类型：1-微信 2-QQ 3-钉钉 4-GitHub 5-其他", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "第三方平台类型不能为空")
    private Integer platformType;

    @Schema(description = "第三方平台用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "第三方平台用户ID不能为空")
    private String platformUserId;

    @Schema(description = "第三方平台用户名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platformUsername;

    @Schema(description = "第三方平台头像", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platformAvatar;

    @Schema(description = "访问令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "访问令牌不能为空")
    private String accessToken;

    @Schema(description = "刷新令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;

    @Schema(description = "令牌过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "令牌过期时间不能为空")
    private LocalDateTime tokenExpireTime;
}
