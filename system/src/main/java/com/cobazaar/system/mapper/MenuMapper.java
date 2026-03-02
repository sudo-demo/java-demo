package com.cobazaar.system.mapper;

import com.cobazaar.system.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单Mapper接口
 * 对应数据库表 sys_menu
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
public interface MenuMapper extends CoreBaseMapper<Menu> {

    /**
     * 根据父菜单ID查询子菜单数量
     *
     * @param parentId 父菜单ID
     * @return 子菜单数量
     */
    Integer countByParentId(@Param("params") Long parentId);

    /**
     * 根据菜单路径查询菜单
     *
     * @param menuPath 菜单路径
     * @return 菜单列表
     */
    List<Menu> selectByMenuPath(@Param("params") String menuPath);

    /**
     * 根据角色ID查询菜单权限
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<Menu> selectByRoleId(@Param("params") Long roleId);

    /**
     * 根据用户ID查询菜单权限
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectByUserId(@Param("params") Long userId);
}
