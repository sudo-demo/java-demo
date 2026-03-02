package com.cobazaar.system.mapper;

import com.cobazaar.system.entity.Department;
import org.apache.ibatis.annotations.Param;

/**
 * 部门Mapper接口
 * 对应数据库表 sys_dept
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
public interface DepartmentMapper extends CoreBaseMapper<Department> {

    /**
     * 根据父部门ID查询子部门数量
     *
     * @param parentId 父部门ID
     * @return 子部门数量
     */
    Integer countByParentId(@Param("params") Long parentId);

    /**
     * 根据部门路径查询部门
     *
     * @param deptPath 部门路径
     * @return 部门列表
     */
    java.util.List<Department> selectByDeptPath(@Param("params") String deptPath);
}
