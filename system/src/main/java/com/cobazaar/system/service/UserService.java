package com.cobazaar.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cobazaar.system.entity.User;
import com.cobazaar.system.dto.UserQueryDTO;
import com.cobazaar.system.vo.UserVO;
import com.cobazaar.common.vo.UserInfoVO;

import java.util.List;

/**
 * 用户服务接口
 * 提供用户相关的业务逻辑操作方法
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据用户ID获取角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getUserRoleIds(Long userId);

    /**
     * 保存用户角色关联
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean saveUserRoles(Long userId, List<Long> roleIds);

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean isUsernameExists(String username, Long excludeId);

    /**
     * 检查手机号是否已存在
     *
     * @param phone 手机号
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean isPhoneExists(String phone, Long excludeId);

    /**
     * 检查邮箱是否已存在
     *
     * @param email 邮箱
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean isEmailExists(String email, Long excludeId);

    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 修改用户密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 更新用户最后登录时间
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean updateLastLoginTime(Long userId);

    /**
     * 分页查询用户列表
     *
     * @param userQueryDTO 用户查询参数
     * @return 用户分页列表
     */
    Page<UserVO> getUserPage(UserQueryDTO userQueryDTO);

    /**
     * 将User实体转换为UserVO
     *
     * @param user 用户实体
     * @return 用户VO
     */
    UserVO convertToVO(User user);

    /**
     * 将User实体列表转换为UserVO列表
     *
     * @param users 用户实体列表
     * @return 用户VO列表
     */
    List<UserVO> convertToVOList(List<User> users);
    
    /**
     * 根据用户ID获取用户详细信息
     *
     * @param userId 用户ID
     * @return 用户详细信息
     */
    UserInfoVO getUserInfoById(Long userId);
    
    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<String> getUserRoles(Long userId);
    
    /**
     * 根据用户ID获取权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getUserPermissions(Long userId);
    
    /**
     * 根据用户ID获取部门信息
     *
     * @param userId 用户ID
     * @return 部门信息
     */
    java.util.Map<String, Object> getUserDepartment(Long userId);
    
    /**
     * 检查用户是否为管理员
     *
     * @param userId 用户ID
     * @return 是否为管理员
     */
    boolean isAdmin(Long userId);

    /**
     * 新增用户
     *
     * @param userAddDTO 新增用户参数
     * @return 是否成功
     */
    boolean addUser(com.cobazaar.system.dto.UserAddDTO userAddDTO);

    /**
     * 修改用户
     *
     * @param updateUserDTO 修改用户参数
     * @return 是否成功
     */
    boolean updateUser(com.cobazaar.system.dto.UpdateUserDTO updateUserDTO);
}
