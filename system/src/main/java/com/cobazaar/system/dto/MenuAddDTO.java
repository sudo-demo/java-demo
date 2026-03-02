package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 新增菜单DTO
 *
 * @author cobazaar
 */
@Schema(name = "MenuAddDTO", description = "新增菜单DTO")
@Data
public class MenuAddDTO {

    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "父菜单ID不能为空")
    private Long parentId;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 2, max = 50, message = "菜单名称长度必须在2-50之间")
    private String menuName;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @Schema(description = "菜单类型（M目录 C菜单 F按钮）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String component;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String perms;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String icon;

    /**
     * 排序
     */
    @Schema(description = "排序", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer sort;

    /**
     * 状态（0正常 1停用）
     */
    @Schema(description = "状态（0正常 1停用）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态不能为空")
    private String status;

    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;
    
    /**
     *是否外链
     */
    @Schema(description = "是否外链", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isExternal;
    
    /**
     *是否缓存
     */
    @Schema(description = "是否缓存", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isCache;
}
