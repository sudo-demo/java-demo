package com.cobazaar.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 菜单VO
 * 用于返回菜单相关的视图数据
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuVO extends BaseVO {
    
    /**
     * 菜单名称
     */
    private String menuName;
    
    /**
     * 父菜单ID
     */
    private Long parentId;
    
    /**
     * 菜单路径
     */
    private String menuPath;
    
    /**
     * 组件路径
     */
    private String componentPath;
    
    /**
     * 菜单类型：M-目录 C-菜单 F-按钮
     */
    private String menuType;
    
    /**
     * 权限标识
     */
    private String perms;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 状态：0-禁用 1-启用
     */
    private String status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 子菜单列表
     */
    private List<MenuVO> children;
}
