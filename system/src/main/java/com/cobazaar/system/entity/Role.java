package com.cobazaar.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 角色实体类
 * 对应数据库表 sys_role
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Schema(name = "Role", description = "角色实体类")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class Role extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
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
    @TableField("role_key")
    private String roleCode;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableField(exist = false)
    private String description;

    /**
     * 排序
     */
    @Schema(description = "排序", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableField("role_sort")
    private Integer sort;

    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 是否默认角色：0-否 1-是
     */
    @Schema(description = "是否默认角色：0-否 1-是", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableField(exist = false)
    private Integer isDefault;

    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;
}
