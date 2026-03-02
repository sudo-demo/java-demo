package com.cobazaar.system.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cobazaar.system.mapper.CoreBaseMapper;
import com.cobazaar.system.entity.User;
import com.cobazaar.system.dto.UserQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper接口
 * 继承CoreBaseMapper获得基础CRUD操作
 *
 * @author cobazaar
 */
@Mapper
public interface UserMapper extends CoreBaseMapper<User> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户实体
     */
    User selectByUsername(@Param("params") String username);
    
    /**
     * 分页查询用户列表
     * @param page 分页对象
     * @param userQueryDTO 查询参数
     * @return 分页结果
     */
    Page<User> selectUserPage(@Param("page") Page<User> page, @Param("params") UserQueryDTO userQueryDTO);
}