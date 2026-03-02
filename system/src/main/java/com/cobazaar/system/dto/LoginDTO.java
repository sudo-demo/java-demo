package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 * 用于接收用户登录时的请求参数
 *
 * @author cobazaar
 */
@Data
@Schema(name = "LoginDTO", description = "用户登录时的请求参数")
public class LoginDTO implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    /**
     * 验证码
     */
    @Schema(description = "验证码", example = "638z", requiredMode = Schema.RequiredMode.REQUIRED)
    private String captcha;

    /**
     * 验证码ID
     */
    @Schema(description = "验证码ID", example = "123456")
    private String captchaId;
}
