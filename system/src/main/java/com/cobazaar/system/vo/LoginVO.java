package com.cobazaar.system.vo;

import com.cobazaar.common.enums.TokenTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import com.cobazaar.common.vo.UserInfoVO;

/**
 * 登录响应VO
 * 用于返回用户登录成功后的响应数据
 *
 * @author cobazaar
 */
@Data
@Schema(name = "LoginVO", description = "用户登录成功后的响应数据")
public class LoginVO {

    /**
     * 响应代码
     */
    @Schema(description = "响应代码", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer code;

    /**
     * 响应消息
     */
    @Schema(description = "响应消息", example = "登录成功", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    /**
     * 用户信息
     */
    @Schema(description = "用户信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserInfoVO user;

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
     * 令牌类型
     */
    @Schema(description = "令牌类型 0: 内置登录，1：统一认证，2：微信登录", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private TokenTypeEnum tokenType = TokenTypeEnum.BUILT_IN;

    /**
     * 过期时间（秒）
     */
    @Schema(description = "过期时间（秒）", example = "3600", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long expiresIn;

}
