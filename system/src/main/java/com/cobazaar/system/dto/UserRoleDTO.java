package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户角色关联DTO
 * 用于保存用户与角色的关联关系
 */
@Schema(name = "UserRoleDTO", description = "用户角色关联DTO")
@Data
public class UserRoleDTO {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 角色ID列表
     */
    @Schema(description = "角色ID列表", required = true)
    @NotNull(message = "角色ID列表不能为空")
    private List<Long> roleIds;
}