package com.cobazaar.system.service;

import com.cobazaar.system.entity.Department;
import com.cobazaar.system.vo.DepartmentVO;
import com.cobazaar.common.service.BaseService;

import java.util.List;

/**
 * 部门Service接口
 * 对应数据库表 sys_dept
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
public interface DepartmentService extends BaseService<Department> {

    /**
     * 获取部门树形结构
     *
     * @return 部门树形列表
     */
    List<Department> getDepartmentTree();

    /**
     * 根据父部门ID查询子部门
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<Department> getChildrenByParentId(Long parentId);

    /**
     * 检查部门名称是否存在
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @param id       部门ID（更新时使用）
     * @return 是否存在
     */
    boolean checkDeptNameExists(String deptName, Long parentId, Long id);

    /**
     * 更新部门状态
     *
     * @param id     部门ID
     * @param status 状态：0-禁用 1-启用
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 构建部门路径
     *
     * @param parentId 父部门ID
     * @return 部门路径
     */
    String buildDeptPath(Long parentId);

    /**
     * 将Department实体转换为DepartmentVO
     *
     * @param department 部门实体
     * @return 部门VO
     */
    DepartmentVO convertToVO(Department department);

    /**
     * 将Department实体列表转换为DepartmentVO列表
     *
     * @param departments 部门实体列表
     * @return 部门VO列表
     */
    List<DepartmentVO> convertToVOList(List<Department> departments);

    /**
     * 将部门树形结构转换为VO树形结构
     *
     * @param departments 部门实体树形列表
     * @return 部门VO树形列表
     */
    List<DepartmentVO> convertTreeToVOList(List<Department> departments);
}
