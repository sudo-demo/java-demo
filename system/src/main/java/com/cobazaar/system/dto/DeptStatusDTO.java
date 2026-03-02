package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 部门状态DTO
 * 用于更新部门的启用/禁用状态
 *
 * @author cobazaar
 */
@Schema(name = "DeptStatusDTO", description = "部门状态DTO")
@Data
public class DeptStatusDTO {
    /**
     * 部门ID
     */
    @Schema(description = "部门ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "部门ID不能为空")
    private Long id;
    
    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态不能为空")
    private Integer status;
}