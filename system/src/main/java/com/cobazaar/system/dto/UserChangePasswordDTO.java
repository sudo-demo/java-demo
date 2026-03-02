package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户修改密码DTO
 * 用于用户修改自己的密码
 */
@Schema(name = "UserChangePasswordDTO", description = "用户修改密码DTO")
@Data
public class UserChangePasswordDTO {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 旧密码
     */
    @Schema(description = "旧密码", required = true)
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    
    /**
     * 新密码
     */
    @Schema(description = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}