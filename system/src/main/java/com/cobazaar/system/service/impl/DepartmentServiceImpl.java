package com.cobazaar.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cobazaar.system.entity.Department;
import com.cobazaar.system.mapper.DepartmentMapper;
import com.cobazaar.system.service.DepartmentService;
import com.cobazaar.system.vo.DepartmentVO;
import com.cobazaar.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门Service实现类
 * 对应数据库表 sys_dept
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentServiceImpl extends BaseServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public List<Department> getDepartmentTree() {
        // 查询所有部门
        List<Department> departments = this.list();
        // 构建树形结构
        return buildDepartmentTree(departments, 0L);
    }

    @Override
    public List<Department> getChildrenByParentId(Long parentId) {
        LambdaQueryWrapper<Department> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Department::getParentId, parentId)
                .orderByAsc(Department::getSort);
        return this.list(wrapper);
    }

    @Override
    public boolean checkDeptNameExists(String deptName, Long parentId, Long id) {
        LambdaQueryWrapper<Department> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Department::getDeptName, deptName)
                .eq(Department::getParentId, parentId);
        if (id != null) {
            wrapper.ne(Department::getId, id);
        }
        return this.count(wrapper) > 0;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        Department department = new Department();
        department.setId(id);
        department.setStatus(status);
        return this.updateById(department);
    }

    @Override
    public String buildDeptPath(Long parentId) {
        if (parentId == 0L) {
            return "0";
        }
        Department parent = this.getById(parentId);
        if (parent == null) {
            return "0";
        }
        return parent.getDeptPath() + "." + parentId;
    }

    @Override
    public boolean save(Department department) {
        // 检查部门名称是否存在
        if (checkDeptNameExists(department.getDeptName(), department.getParentId(), null)) {
            throw new com.cobazaar.common.exception.ServiceException("部门名称已存在");
        }
        // 构建部门路径
        String deptPath = buildDeptPath(department.getParentId());
        department.setDeptPath(deptPath);
        return super.save(department);
    }

    @Override
    public boolean updateById(Department department) {
        // 检查部门名称是否存在
        if (checkDeptNameExists(department.getDeptName(), department.getParentId(), department.getId())) {
            throw new com.cobazaar.common.exception.ServiceException("部门名称已存在");
        }
        // 如果父部门变更，需要重新构建部门路径
        Department oldDept = this.getById(department.getId());
        if (oldDept != null && !oldDept.getParentId().equals(department.getParentId())) {
            String deptPath = buildDeptPath(department.getParentId());
            department.setDeptPath(deptPath);
            // 更新子部门的路径
            updateChildrenDeptPath(oldDept.getDeptPath(), deptPath, department.getId());
        }
        return super.updateById(department);
    }

    @Override
    public boolean removeById(java.io.Serializable id) {
        // 检查是否有子部门
        int count = baseMapper.countByParentId((Long) id);
        if (count > 0) {
            log.warn("部门 {} 存在子部门，无法删除", id);
            return false;
        }
        return super.removeById(id);
    }

    /**
     * 构建部门树形结构
     *
     * @param departments 部门列表
     * @param parentId    父部门ID
     * @return 部门树形列表
     */
    private List<Department> buildDepartmentTree(List<Department> departments, Long parentId) {
        List<Department> treeList = new ArrayList<>();
        for (Department dept : departments) {
            if (parentId.equals(dept.getParentId())) {
                List<Department> children = buildDepartmentTree(departments, dept.getId());
                // 这里可以通过扩展属性存储子部门
                treeList.add(dept);
            }
        }
        return treeList;
    }

    /**
     * 更新子部门的路径
     *
     * @param oldPath 旧路径
     * @param newPath 新路径
     * @param deptId  部门ID
     */
    private void updateChildrenDeptPath(String oldPath, String newPath, Long deptId) {
        List<Department> children = baseMapper.selectByDeptPath(oldPath + "." + deptId + "%");
        for (Department child : children) {
            String childPath = child.getDeptPath();
            String newChildPath = childPath.replace(oldPath, newPath);
            child.setDeptPath(newChildPath);
            this.updateById(child);
        }
    }

    @Override
    public DepartmentVO convertToVO(Department department) {
        if (department == null) {
            return null;
        }
        DepartmentVO departmentVO = new DepartmentVO();
        departmentVO.setId(department.getId());
        departmentVO.setDeptName(department.getDeptName());
        departmentVO.setParentId(department.getParentId());
        departmentVO.setDeptPath(department.getDeptPath());
        departmentVO.setSort(department.getSort());
        departmentVO.setStatus(department.getStatus());
        departmentVO.setRemark(department.getRemark());
        departmentVO.setCreateTime(department.getCreateTime());
        departmentVO.setUpdateTime(department.getUpdateTime());
        return departmentVO;
    }

    @Override
    public List<DepartmentVO> convertToVOList(List<Department> departments) {
        if (departments == null || departments.isEmpty()) {
            return new ArrayList<>();
        }
        return departments.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<DepartmentVO> convertTreeToVOList(List<Department> departments) {
        if (departments == null || departments.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<DepartmentVO> voList = new ArrayList<>();
        for (Department dept : departments) {
            DepartmentVO vo = convertToVO(dept);
            // 递归处理子部门
            List<Department> children = getChildrenByParentId(dept.getId());
            if (!children.isEmpty()) {
                vo.setChildren(convertTreeToVOList(children));
            }
            voList.add(vo);
        }
        return voList;
    }
}
