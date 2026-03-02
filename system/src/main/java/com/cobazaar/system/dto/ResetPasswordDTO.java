package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 重置密码DTO
 * 用于重置用户的密码
 */
@Schema(name = "ResetPasswordDTO", description = "重置密码DTO")
@Data
public class ResetPasswordDTO {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 新密码
     */
    @Schema(description = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    private String password;
}