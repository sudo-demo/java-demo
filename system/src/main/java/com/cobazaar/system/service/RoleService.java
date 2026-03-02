package com.cobazaar.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cobazaar.system.entity.Role;
import com.cobazaar.system.dto.RoleQueryDTO;
import com.cobazaar.system.vo.RoleVO;

import java.util.List;

/**
 * 角色服务接口
 * 提供角色相关的业务逻辑操作方法
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getRolesByUserId(Long userId);

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    List<Role> getRoleList();

    /**
     * 保存角色菜单权限
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     * @return 是否成功
     */
    boolean saveRoleMenus(Long roleId, List<Long> menuIds);

    /**
     * 获取角色的菜单权限ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getRoleMenuIds(Long roleId);

    /**
     * 检查角色名称是否已存在
     *
     * @param roleName 角色名称
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean isRoleNameExists(String roleName, Long excludeId);

    /**
     * 检查角色代码是否已存在
     *
     * @param roleCode 角色代码
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean isRoleCodeExists(String roleCode, Long excludeId);
    
    /**
     * 分页查询角色列表
     *
     * @param roleQueryDTO 角色查询参数
     * @return 角色分页列表
     */
    Page<RoleVO> getRolePage(RoleQueryDTO roleQueryDTO);
    
    /**
     * 将Role实体转换为RoleVO
     *
     * @param role 角色实体
     * @return 角色VO
     */
    RoleVO convertToVO(Role role);
    
    /**
     * 将Role实体列表转换为RoleVO列表
     *
     * @param roles 角色实体列表
     * @return 角色VO列表
     */
    /**
     * 将Role实体列表转换为RoleVO列表
     *
     * @param roles 角色实体列表
     * @return 角色VO列表
     */
    List<RoleVO> convertToVOList(List<Role> roles);

    /**
     * 新增角色
     *
     * @param roleAddDTO 新增角色参数
     * @return 是否成功
     */
    boolean addRole(com.cobazaar.system.dto.RoleAddDTO roleAddDTO);

    /**
     * 修改角色
     *
     * @param updateRoleDTO 修改角色参数
     * @return 是否成功
     */
    boolean updateRole(com.cobazaar.system.dto.UpdateRoleDTO updateRoleDTO);
}
