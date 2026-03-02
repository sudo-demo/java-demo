package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 更新角色DTO
 * 用于修改角色信息
 *
 * @author cobazaar
 */
@Schema(name = "UpdateRoleDTO", description = "更新角色DTO")
@Data
public class UpdateRoleDTO {
    /**
     * 角色ID
     */
    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色ID不能为空")
    private Long id;
    
    /**
     * 角色名称
     */
    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 1, max = 20, message = "角色名称长度必须在1-20之间")
    private String roleName;
    
    /**
     * 角色代码
     */
    @Schema(description = "角色代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "角色代码不能为空")
    @Size(min = 1, max = 20, message = "角色代码长度必须在1-20之间")
    private String roleCode;
    
    /**
     * 角色描述
     */
    @Schema(description = "角色描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;
    
    /**
     * 排序
     */
    @Schema(description = "排序", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer sort;
    
    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态不能为空")
    private Integer status;
    
    /**
     * 是否默认角色：0-否 1-是
     */
    @Schema(description = "是否默认角色：0-否 1-是", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isDefault;
    
    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;
}