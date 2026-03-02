package com.cobazaar.system.controller;

import com.cobazaar.system.entity.Department;
import com.cobazaar.system.service.DepartmentService;
import com.cobazaar.system.vo.DepartmentVO;
import com.cobazaar.common.result.Result;
import com.cobazaar.system.dto.DeptStatusDTO;
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
 * 部门Controller
 * 处理部门相关的HTTP请求
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Slf4j
@RestController
@RequestMapping("/dept")
@Tag(name = "部门管理")
@RequiredArgsConstructor
public class DepartmentController extends BaseController {

    private final DepartmentService departmentService;

    /**
     * 获取部门树形结构
     *
     * @return 部门树形列表
     */
    @GetMapping("/tree")
    @Operation(summary = "获取部门树形结构", description = "获取完整的部门树形结构")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public Result<List<DepartmentVO>> getDepartmentTree() {
        List<Department> treeList = departmentService.getDepartmentTree();
        List<DepartmentVO> voList = departmentService.convertTreeToVOList(treeList);
        return Result.success(voList);
    }

    /**
     * 根据父部门ID查询子部门
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    @GetMapping("/children")
    @Operation(summary = "查询子部门", description = "根据父部门ID查询子部门列表")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public Result<List<DepartmentVO>> getChildrenByParentId(
            @Parameter(name = "parentId", description = "父部门ID", required = true) @RequestParam Long parentId) {
        List<Department> children = departmentService.getChildrenByParentId(parentId);
        List<DepartmentVO> voList = departmentService.convertToVOList(children);
        return Result.success(voList);
    }

    /**
     * 获取部门详情
     *
     * @param id 部门ID
     * @return 部门详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取部门详情", description = "根据部门ID获取部门详细信息")
    @PreAuthorize("hasAuthority('system:dept:query')")
    public Result<DepartmentVO> getById(
            @Parameter(name = "id", description = "部门ID", required = true) @PathVariable Long id) {
        Department department = departmentService.getById(id);
        if (department == null) {
            return Result.error("部门不存在");
        }
        DepartmentVO departmentVO = departmentService.convertToVO(department);
        return Result.success(departmentVO);
    }

    /**
     * 检查部门名称是否存在
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @param id       部门ID（更新时使用）
     * @return 是否存在
     */
    @GetMapping("/check-name")
    @Operation(summary = "检查部门名称", description = "检查部门名称是否已存在")
    @PreAuthorize("hasAuthority('system:dept:query')")
    public Result<Boolean> checkDeptNameExists(
            @Parameter(name = "deptName", description = "部门名称", required = true) @RequestParam String deptName,
            @Parameter(name = "parentId", description = "父部门ID", required = true) @RequestParam Long parentId,
            @Parameter(name = "id", description = "部门ID（更新时使用）") @RequestParam(required = false) Long id) {
        boolean exists = departmentService.checkDeptNameExists(deptName, parentId, id);
        return Result.success(exists);
    }

    /**
     * 新增部门
     *
     * @param department 部门信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "新增部门", description = "创建新的部门")
    @PreAuthorize("hasAuthority('system:dept:add')")
    public Result<Boolean> save(@Parameter(name = "department", description = "部门信息", required = true) @Valid @RequestBody Department department) {
        boolean success = departmentService.save(department);
        return success ? Result.success(true) : Result.error("新增部门失败");
    }

    /**
     * 更新部门
     *
     * @param department 部门信息
     * @return 操作结果
     */
    @PostMapping("/update")
    @Operation(summary = "更新部门", description = "更新部门信息")
    @PreAuthorize("hasAuthority('system:dept:edit')")
    public Result<Boolean> update(@Parameter(name = "department", description = "部门信息", required = true) @Valid @RequestBody Department department) {
        boolean success = departmentService.updateById(department);
        return success ? Result.success(true) : Result.error("更新部门失败");
    }

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "删除部门", description = "根据部门ID删除部门")
    @PreAuthorize("hasAuthority('system:dept:remove')")
    public Result<Boolean> removeById(
            @Parameter(name = "id", description = "部门ID", required = true) @PathVariable Long id) {
        boolean success = departmentService.removeById(id);
        if (!success) {
            return Result.error("部门存在子部门，无法删除");
        }
        return Result.success(true);
    }

    /**
     * 更新部门状态
     *
     * @param id     部门ID
     * @param status 状态：0-禁用 1-启用
     * @return 操作结果
     */
    @PostMapping("/update/status")
    @Operation(summary = "更新部门状态", description = "更新部门的启用/禁用状态")
    @PreAuthorize("hasAuthority('system:dept:edit')")
    public Result<Boolean> updateStatus(
            @Parameter(name = "deptStatusDTO", description = "部门状态更新信息", required = true) @Valid @RequestBody DeptStatusDTO deptStatusDTO) {
        boolean success = departmentService.updateStatus(deptStatusDTO.getId(), deptStatusDTO.getStatus());
        return success ? Result.success(true) : Result.error("更新部门状态失败");
    }
}
