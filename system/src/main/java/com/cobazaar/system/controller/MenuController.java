package com.cobazaar.system.controller;

import com.cobazaar.system.entity.Menu;
import com.cobazaar.system.service.MenuService;
import com.cobazaar.system.vo.MenuVO;
import com.cobazaar.common.result.Result;
import com.cobazaar.system.dto.MenuStatusDTO;
import com.cobazaar.system.dto.MenuAddDTO;
import com.cobazaar.system.dto.UpdateMenuDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单Controller
 * 处理菜单相关的HTTP请求
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@Tag(name = "菜单管理")
@RequiredArgsConstructor
public class MenuController extends BaseController {

    private final MenuService menuService;

    /**
     * 获取菜单树形结构
     *
     * @return 菜单树形列表
     */
    @GetMapping("/tree")
    @Operation(summary = "获取菜单树形结构", description = "获取完整的菜单树形结构")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<MenuVO>> getMenuTree() {
        List<Menu> treeList = menuService.getMenuTree();
        List<MenuVO> voList = menuService.convertTreeToVOList(treeList);
        return Result.success(voList);
    }

    /**
     * 根据父菜单ID查询子菜单
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    @GetMapping("/children")
    @Operation(summary = "查询子菜单", description = "根据父菜单ID查询子菜单列表")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<MenuVO>> getChildrenByParentId(
            @Parameter(name = "parentId", description = "父菜单ID", required = true) @RequestParam Long parentId) {
        List<Menu> children = menuService.getChildrenByParentId(parentId);
        List<MenuVO> voList = menuService.convertToVOList(children);
        return Result.success(voList);
    }

    /**
     * 根据角色ID查询菜单权限
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    @GetMapping("/role/{roleId}")
    @Operation(summary = "查询角色菜单权限", description = "根据角色ID查询菜单权限列表")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<MenuVO>> getMenusByRoleId(
            @Parameter(name = "roleId", description = "角色ID", required = true) @PathVariable Long roleId) {
        List<Menu> menus = menuService.getMenusByRoleId(roleId);
        List<MenuVO> voList = menuService.convertToVOList(menus);
        return Result.success(voList);
    }

    /**
     * 获取菜单详情
     *
     * @param id 菜单ID
     * @return 菜单详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取菜单详情", description = "根据菜单ID获取菜单详细信息")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Result<MenuVO> getById(
            @Parameter(name = "id", description = "菜单ID", required = true) @PathVariable Long id) {
        Menu menu = menuService.getById(id);
        if (menu == null) {
            return Result.error("菜单不存在");
        }
        MenuVO menuVO = menuService.convertToVO(menu);
        return Result.success(menuVO);
    }

    /**
     * 检查菜单名称是否存在
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @param menuType 菜单类型
     * @param id       菜单ID（更新时使用）
     * @return 是否存在
     */
    @GetMapping("/check-name")
    @Operation(summary = "检查菜单名称", description = "检查菜单名称是否已存在")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Result<Boolean> checkMenuNameExists(
            @Parameter(name = "menuName", description = "菜单名称", required = true) @RequestParam String menuName,
            @Parameter(name = "parentId", description = "父菜单ID", required = true) @RequestParam Long parentId,
            @Parameter(name = "menuType", description = "菜单类型", required = true) @RequestParam String menuType,
            @Parameter(name = "id", description = "菜单ID（更新时使用）") @RequestParam(required = false) Long id) {
        boolean exists = menuService.checkMenuNameExists(menuName, parentId, menuType, id);
        return Result.success(exists);
    }

    /**
     * 新增菜单
     *
     * @param menuAddDTO 菜单信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "新增菜单", description = "创建新的菜单")
    @PreAuthorize("hasAuthority('system:menu:add')")
    public Result<Boolean> save(@Parameter(name = "menuAddDTO", description = "菜单信息", required = true) @Valid @RequestBody MenuAddDTO menuAddDTO) {
        boolean success = menuService.saveMenu(menuAddDTO);
        return success ? Result.success(true) : Result.error("新增菜单失败");
    }

    /**
     * 更新菜单
     *
     * @param updateMenuDTO 菜单信息
     * @return 操作结果
     */
    @PostMapping("/update")
    @Operation(summary = "更新菜单", description = "更新菜单信息")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Result<Boolean> update(@Parameter(name = "updateMenuDTO", description = "菜单信息", required = true) @Valid @RequestBody UpdateMenuDTO updateMenuDTO) {
        boolean success = menuService.updateMenu(updateMenuDTO);
        return success ? Result.success(true) : Result.error("更新菜单失败");
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "删除菜单", description = "根据菜单ID删除菜单")
    @PreAuthorize("hasAuthority('system:menu:remove')")
    public Result<Boolean> removeById(
            @Parameter(name = "id", description = "菜单ID", required = true) @PathVariable Long id) {
        boolean success = menuService.removeById(id);
        if (!success) {
            return Result.error("菜单存在子菜单，无法删除");
        }
        return Result.success(true);
    }

    /**
     * 更新菜单状态
     *
     * @param id     菜单ID
     * @param status 状态：0-禁用 1-启用
     * @return 操作结果
     */
    @PostMapping("/status")
    @Operation(summary = "更新菜单状态", description = "更新菜单的启用/禁用状态")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Result<Boolean> updateStatus(
            @Parameter(name = "menuStatusDTO", description = "菜单状态更新信息", required = true) @Valid @RequestBody MenuStatusDTO menuStatusDTO) {
        boolean success = menuService.updateStatus(menuStatusDTO.getId(), menuStatusDTO.getStatus());
        return success ? Result.success(true) : Result.error("更新菜单状态失败");
    }

    /**
     * 获取用户的菜单权限标识
     *
     * @param userId 用户ID
     * @return 权限标识列表
     */
    @GetMapping("/permissions/{userId}")
    @Operation(summary = "获取用户权限", description = "根据用户ID获取菜单权限标识列表")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Result<List<String>> getUserPermissions(
            @Parameter(name = "userId", description = "用户ID", required = true) @PathVariable Long userId) {
        List<String> permissions = menuService.getUserPermissions(userId);
        return Result.success(permissions);
    }
}
