package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 菜单状态DTO
 * 用于更新菜单的启用/禁用状态
 *
 * @author cobazaar
 */
@Schema(name = "MenuStatusDTO", description = "菜单状态DTO")
@Data
public class MenuStatusDTO {
    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "菜单ID不能为空")
    private Long id;
    
    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态不能为空")
    private String status;
}