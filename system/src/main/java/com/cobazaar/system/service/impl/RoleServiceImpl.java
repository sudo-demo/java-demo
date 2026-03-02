package com.cobazaar.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cobazaar.system.entity.Role;
import com.cobazaar.system.mapper.RoleMapper;
import com.cobazaar.system.service.RoleService;
import com.cobazaar.system.dto.RoleQueryDTO;
import com.cobazaar.system.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 * 实现角色相关的业务逻辑操作
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        // 这里需要实现通过用户角色关联表查询，暂时返回空列表，后续需要实现
        return java.util.Collections.emptyList();
    }

    @Override
    public List<Role> getRoleList() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "role_name", "role_key", "role_sort", "status", "remark");
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRoleMenus(Long roleId, List<Long> menuIds) {
        // 这里需要实现角色菜单权限的保存逻辑，暂时返回true，后续需要实现
        return true;
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        // 这里需要实现获取角色菜单权限ID列表的逻辑，暂时返回空列表，后续需要实现
        return java.util.Collections.emptyList();
    }

    @Override
    public boolean isRoleNameExists(String roleName, Long excludeId) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleName, roleName);
        if (excludeId != null) {
            queryWrapper.ne(Role::getId, excludeId);
        }
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean isRoleCodeExists(String roleCode, Long excludeId) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_key", roleCode);
        if (excludeId != null) {
            queryWrapper.ne("id", excludeId);
        }
        return count(queryWrapper) > 0;
    }
    
    @Override
    public Page<RoleVO> getRolePage(RoleQueryDTO roleQueryDTO) {
        // 创建分页对象
        Page<Role> page = new Page<>(roleQueryDTO.getPage(), roleQueryDTO.getPageSize());
        
        // 执行XML方式的分页查询
        Page<Role> resultPage = roleMapper.selectRolePage(page, roleQueryDTO);
        
        // 转换为VO分页对象
        Page<RoleVO> voPage = new Page<>();
        voPage.setRecords(convertToVOList(resultPage.getRecords()));
        voPage.setTotal(resultPage.getTotal());
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setPages(resultPage.getPages());
        
        return voPage;
    }
    
    @Override
    public RoleVO convertToVO(Role role) {
        if (role == null) {
            return null;
        }
        RoleVO roleVO = new RoleVO();
        roleVO.setId(role.getId());
        roleVO.setRoleName(role.getRoleName());
        roleVO.setRoleCode(role.getRoleCode());
        roleVO.setDescription(role.getDescription());
        roleVO.setStatus(role.getStatus());
        roleVO.setRemark(role.getRemark());
        return roleVO;
    }
    
    @Override
    public List<RoleVO> convertToVOList(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }
        return roles.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRole(com.cobazaar.system.dto.RoleAddDTO roleAddDTO) {
        // 检查角色名称是否已存在
        if (isRoleNameExists(roleAddDTO.getRoleName(), null)) {
            throw new com.cobazaar.common.exception.ServiceException("角色名称已存在");
        }
        // 检查角色代码是否已存在
        if (isRoleCodeExists(roleAddDTO.getRoleCode(), null)) {
            throw new com.cobazaar.common.exception.ServiceException("角色代码已存在");
        }

        Role role = new Role();
        // 复制属性
        cn.hutool.core.bean.BeanUtil.copyProperties(roleAddDTO, role);
        role.setRoleCode(roleAddDTO.getRoleCode()); // 确保roleCode被设置（DTO中叫roleCode，Entity中叫roleCode，对应数据库role_key）
        
        // 设置默认值
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        // 创建时间等由MyBatis Plus自动填充或数据库默认值处理，或者在这里手动设置
        // role.setCreateTime(LocalDateTime.now());
        // role.setUpdateTime(LocalDateTime.now());

        return save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(com.cobazaar.system.dto.UpdateRoleDTO updateRoleDTO) {
        Long id = updateRoleDTO.getId();
        Role existRole = getById(id);
        if (existRole == null) {
            throw new com.cobazaar.common.exception.ServiceException("角色不存在");
        }

        // 检查角色名称是否已存在
        if (isRoleNameExists(updateRoleDTO.getRoleName(), id)) {
            throw new com.cobazaar.common.exception.ServiceException("角色名称已存在");
        }
        // 检查角色代码是否已存在
        if (isRoleCodeExists(updateRoleDTO.getRoleCode(), id)) {
            throw new com.cobazaar.common.exception.ServiceException("角色代码已存在");
        }

        Role role = new Role();
        role.setId(id);
        role.setRoleName(updateRoleDTO.getRoleName());
        role.setRoleCode(updateRoleDTO.getRoleCode());
        role.setDescription(updateRoleDTO.getDescription());
        role.setSort(updateRoleDTO.getSort());
        role.setStatus(updateRoleDTO.getStatus());
        role.setIsDefault(updateRoleDTO.getIsDefault());
        role.setRemark(updateRoleDTO.getRemark());
        // 更新时间通常由自动填充处理

        return updateById(role);
    }
}
