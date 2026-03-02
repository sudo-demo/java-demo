package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 删除用户DTO
 * 用于删除用户
 *
 * @author cobazaar
 */
@Schema(name = "DeleteUserDTO", description = "删除用户DTO")
@Data
public class DeleteUserDTO {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户ID不能为空")
    private Long id;
}