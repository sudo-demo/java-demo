package com.cobazaar.system.service;

import com.cobazaar.system.entity.Menu;
import com.cobazaar.system.vo.MenuVO;
import com.cobazaar.common.service.BaseService;

import java.util.List;

/**
 * 菜单Service接口
 * 对应数据库表 sys_menu
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
public interface MenuService extends BaseService<Menu> {

    /**
     * 获取菜单树形结构
     *
     * @return 菜单树形列表
     */
    List<Menu> getMenuTree();

    /**
     * 根据父菜单ID查询子菜单
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    List<Menu> getChildrenByParentId(Long parentId);

    /**
     * 根据角色ID查询菜单权限
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<Menu> getMenusByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单权限
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> getMenusByUserId(Long userId);

    /**
     * 检查菜单名称是否存在
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @param menuType 菜单类型
     * @param id       菜单ID（更新时使用）
     * @return 是否存在
     */
    boolean checkMenuNameExists(String menuName, Long parentId, String menuType, Long id);

    /**
     * 更新菜单状态
     *
     * @param id     菜单ID
     * @param status 状态：0-禁用 1-启用
     * @return 是否成功
     */
    boolean updateStatus(Long id, String status);

    /**
     * 构建菜单路径
     *
     * @param parentId 父菜单ID
     * @return 菜单路径
     */
    String buildMenuPath(Long parentId);

    /**
     * 获取用户的菜单权限标识
     *
     * @param userId 用户ID
     * @return 权限标识列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 将Menu实体转换为MenuVO
     *
     * @param menu 菜单实体
     * @return 菜单VO
     */
    MenuVO convertToVO(Menu menu);

    /**
     * 将Menu实体列表转换为MenuVO列表
     *
     * @param menus 菜单实体列表
     * @return 菜单VO列表
     */
    List<MenuVO> convertToVOList(List<Menu> menus);

    /**
     * 将菜单树形结构转换为VO树形结构
     *
     * @param menus 菜单实体树形列表
     * @return 菜单VO树形列表
     */
    List<MenuVO> convertTreeToVOList(List<Menu> menus);

    /**
     * 新增菜单
     *
     * @param menuAddDTO 新增菜单参数
     * @return 是否成功
     */
    boolean saveMenu(com.cobazaar.system.dto.MenuAddDTO menuAddDTO);

    /**
     * 更新菜单
     *
     * @param updateMenuDTO 更新菜单参数
     * @return 是否成功
     */
    boolean updateMenu(com.cobazaar.system.dto.UpdateMenuDTO updateMenuDTO);
}
