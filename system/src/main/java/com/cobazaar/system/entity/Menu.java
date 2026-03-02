package com.cobazaar.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.cobazaar.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 菜单实体类
 * 对应数据库表 sys_menu
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Schema(name = "Menu", description = "菜单实体类")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class Menu extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", required = true)
    private Long id;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称", required = true)
    private String menuName;

    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID", required = true)
    private Long parentId;

    /**
     * 菜单路径
     */
    @Schema(description = "菜单路径", required = false)
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径", required = false)
    private String component;

    /**
     * 菜单类型：M-目录 C-菜单 F-按钮
     */
    @Schema(description = "菜单类型：M-目录 C-菜单 F-按钮", required = true)
    @TableField("menu_type")
    private String menuType;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标", required = false)
    private String icon;

    /**
     * 排序
     */
    @Schema(description = "排序", required = false)
    @TableField("order_num")
    private Integer sort;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识", required = false)
    private String perms;

    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", required = true)
    @TableField("status")
    private String status;

    /**
     * 菜单路径
     */
    @Schema(description = "菜单路径", required = false)
    @TableField("path")
    private String menuPath;

    /**
     * 是否外链：0-否 1-是
     */
    @Schema(description = "是否外链：0-否 1-是", required = false)
    @TableField("is_frame")
    private Integer isExternal;

    /**
     * 是否缓存：0-否 1-是
     */
    @Schema(description = "是否缓存：0-否 1-是", required = false)
    @TableField("is_cache")
    private Integer isCache;

    /**
     * 备注
     */
    @Schema(description = "备注", required = false)
    private String remark;
}
