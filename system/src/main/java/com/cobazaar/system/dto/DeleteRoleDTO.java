package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 删除角色DTO
 * 用于删除角色
 *
 * @author cobazaar
 */
@Schema(name = "DeleteRoleDTO", description = "删除角色DTO")
@Data
public class DeleteRoleDTO {
    /**
     * 角色ID
     */
    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色ID不能为空")
    private Long id;
}