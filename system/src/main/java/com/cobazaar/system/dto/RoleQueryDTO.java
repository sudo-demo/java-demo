package com.cobazaar.system.dto;

import com.cobazaar.common.dto.QueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色查询DTO
 * 用于接收角色列表查询的参数
 */
@Schema(name = "RoleQueryDTO", description = "角色查询DTO")
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleQueryDTO extends QueryDTO {
    /**
     * 角色名称
     */
    @Schema(description = "角色名称", required = false)
    private String roleName;
    
    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", required = false)
    private Integer status;
}
