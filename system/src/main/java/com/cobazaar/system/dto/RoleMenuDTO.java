package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色菜单权限DTO
 * 用于保存角色的菜单权限
 *
 * @author cobazaar
 */
@Schema(name = "RoleMenuDTO", description = "角色菜单权限DTO")
@Data
public class RoleMenuDTO {
    /**
     * 角色ID
     */
    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
    
    /**
     * 菜单ID列表
     */
    @Schema(description = "菜单ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "菜单ID列表不能为空")
    private List<Long> menuIds;
}