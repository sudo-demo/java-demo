package com.cobazaar.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cobazaar.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 部门实体类
 * 对应数据库表 sys_dept
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Schema(name = "Department", description = "部门实体类")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class Department extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", required = true)
    private Long id;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称", required = true)
    private String deptName;

    /**
     * 父部门ID
     */
    @Schema(description = "父部门ID", required = true)
    private Long parentId;

    /**
     * 部门负责人
     */
    @Schema(description = "部门负责人", required = false)
    private String leader;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话", required = false)
    private String phone;

    /**
     * 部门邮箱
     */
    @Schema(description = "部门邮箱", required = false)
    private String email;

    /**
     * 部门排序
     */
    @Schema(description = "部门排序", required = false)
    private Integer sort;

    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", required = true)
    private Integer status;

    /**
     * 部门路径
     */
    @Schema(description = "部门路径", required = false)
    private String deptPath;

    /**
     * 备注
     */
    @Schema(description = "备注", required = false)
    private String remark;
}
