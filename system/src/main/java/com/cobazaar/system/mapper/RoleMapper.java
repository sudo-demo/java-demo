package com.cobazaar.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cobazaar.system.entity.Role;
import com.cobazaar.system.dto.RoleQueryDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 角色Mapper接口
 * 提供角色相关的数据库操作方法
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 分页查询角色列表
     * @param page 分页对象
     * @param roleQueryDTO 查询参数
     * @return 分页结果
     */
    Page<Role> selectRolePage(@Param("page") Page<Role> page, @Param("params") RoleQueryDTO roleQueryDTO);
}
