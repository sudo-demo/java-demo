package com.cobazaar.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 部门VO
 * 用于返回部门相关的视图数据
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DepartmentVO extends BaseVO {
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 父部门ID
     */
    private Long parentId;
    
    /**
     * 部门路径
     */
    private String deptPath;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 子部门列表
     */
    private List<DepartmentVO> children;
}
