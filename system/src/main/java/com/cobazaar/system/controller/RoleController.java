package com.cobazaar.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cobazaar.common.result.Result;
import com.cobazaar.system.entity.Role;
import com.cobazaar.system.service.RoleService;
import com.cobazaar.system.dto.RoleAddDTO;
import com.cobazaar.system.dto.RoleQueryDTO;
import com.cobazaar.system.dto.RoleMenuDTO;
import com.cobazaar.system.dto.UpdateRoleDTO;
import com.cobazaar.system.dto.DeleteRoleDTO;
import com.cobazaar.system.vo.RoleVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理控制器
 * 提供角色相关的RESTful API接口
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 获取角色列表（分页）
     *
     * @param roleQueryDTO 角色查询参数
     * @return 角色列表
     */
    @GetMapping("/page")
    @Operation(summary = "获取角色列表（分页）", description = "根据查询参数获取角色列表，支持分页")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<Page<RoleVO>> getRolePage(
            @Parameter(name = "roleQueryDTO", description = "角色查询参数", required = true) @ModelAttribute RoleQueryDTO roleQueryDTO) {
        // 调用Service层方法进行分页查询
        Page<RoleVO> rolePage = roleService.getRolePage(roleQueryDTO);
        return Result.success(rolePage);
    }

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取所有角色列表", description = "获取系统中所有角色的列表")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<List<RoleVO>> getRoleList() {
        List<Role> roles = roleService.getRoleList();
        List<RoleVO> roleVOList = roleService.convertToVOList(roles);
        return Result.success(roleVOList);
    }

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取角色详情", description = "根据角色ID获取角色的详细信息")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<RoleVO> getRoleById(@Parameter(name = "id", description = "角色ID", required = true) @PathVariable Long id) {
        Role role = roleService.getById(id);
        RoleVO roleVO = roleService.convertToVO(role);
        return Result.success(roleVO);
    }

    /**
     * 新增角色
     *
     * @param roleAddDTO 角色信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "新增角色", description = "创建新的角色")
    @PreAuthorize("hasAuthority('system:role:add')")
    public Result<?> addRole(@Parameter(name = "roleAddDTO", description = "角色信息", required = true) @Valid @RequestBody RoleAddDTO roleAddDTO) {
        roleService.addRole(roleAddDTO);
        return Result.success("新增角色成功");
    }

    /**
     * 修改角色
     *
     * @param updateRoleDTO 更新角色信息
     * @return 操作结果
     */
    @PostMapping("/update")
    @Operation(summary = "修改角色", description = "更新角色信息")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<?> updateRole(@Parameter(name = "updateRoleDTO", description = "更新角色信息", required = true) @Valid @RequestBody UpdateRoleDTO updateRoleDTO) {
        roleService.updateRole(updateRoleDTO);
        return Result.success("修改角色成功");
    }

    /**
     * 删除角色
     *
     * @param deleteRoleDTO 删除角色信息
     * @return 操作结果
     */
    @PostMapping("/delete")
    @Operation(summary = "删除角色", description = "根据角色ID删除角色")
    @PreAuthorize("hasAuthority('system:role:remove')")
    public Result<?> deleteRole(@Parameter(name = "deleteRoleDTO", description = "删除角色信息", required = true) @Valid @RequestBody DeleteRoleDTO deleteRoleDTO) {
        roleService.removeById(deleteRoleDTO.getId());
        return Result.success("删除角色成功");
    }

    /**
     * 批量删除角色
     *
     * @param ids 角色ID列表
     * @return 操作结果
     */
    @PostMapping("/delete/batch")
    @Operation(summary = "批量删除角色", description = "根据角色ID列表批量删除角色")
    @PreAuthorize("hasAuthority('system:role:remove')")
    public Result<?> batchDeleteRole(@Parameter(name = "ids", description = "角色ID列表", required = true) @RequestBody List<Long> ids) {
        roleService.removeByIds(ids);
        return Result.success("批量删除角色成功");
    }

    /**
     * 获取角色的菜单权限
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    @GetMapping("/menu/{roleId}")
    @Operation(summary = "获取角色菜单权限", description = "根据角色ID获取角色的菜单权限")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<List<Long>> getRoleMenuIds(@Parameter(name = "roleId", description = "角色ID", required = true) @PathVariable Long roleId) {
        List<Long> menuIds = roleService.getRoleMenuIds(roleId);
        return Result.success(menuIds);
    }

    /**
     * 保存角色的菜单权限
     *
     * @return 操作结果
     */
    @PostMapping("/menu")
    @Operation(summary = "保存角色菜单权限", description = "保存角色的菜单权限")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<?> saveRoleMenus(
            @Parameter(name = "roleMenuDTO", description = "角色菜单权限信息", required = true) @Valid @RequestBody RoleMenuDTO roleMenuDTO) {
        roleService.saveRoleMenus(roleMenuDTO.getRoleId(), roleMenuDTO.getMenuIds());
        return Result.success("保存角色权限成功");
    }
}
