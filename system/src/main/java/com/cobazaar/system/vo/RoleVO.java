package com.cobazaar.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色VO
 * 用于返回角色信息
 *
 * @author cobazaar
 */
@Schema(name = "RoleVO", description = "角色VO")
@Data
public class RoleVO {
    /**
     * 角色ID
     */
    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    
    /**
     * 角色名称
     */
    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleName;
    
    /**
     * 角色代码
     */
    @Schema(description = "角色代码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleCode;
    
    /**
     * 角色描述
     */
    @Schema(description = "角色描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;
    
    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
    
    /**
     * 创建时间
     */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createTime;
    
    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;
}
