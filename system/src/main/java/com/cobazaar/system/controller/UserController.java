package com.cobazaar.system.controller;

import com.cobazaar.system.dto.UserAddDTO;
import com.cobazaar.system.dto.UserChangePasswordDTO;
import com.cobazaar.system.dto.UserQueryDTO;
import com.cobazaar.system.dto.UserRoleDTO;
import com.cobazaar.system.dto.UpdateUserDTO;
import com.cobazaar.system.dto.DeleteUserDTO;
import com.cobazaar.system.dto.ResetPasswordDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cobazaar.common.result.Result;
import com.cobazaar.system.entity.User;
import com.cobazaar.system.service.UserService;
import com.cobazaar.system.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户管理控制器
 * 提供用户相关的RESTful API接口
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Tag(name = "用户管理", description = "提供用户相关的RESTful API接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取用户列表（分页）
     *
     * @param userQueryDTO 用户查询参数
     * @return 用户列表
     */
    @GetMapping("/page")
    @Operation(summary = "获取用户列表（分页）", description = "根据查询参数获取用户列表，支持分页")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<Page<UserVO>> getUserPage(@Parameter(name = "userQueryDTO", description = "用户查询参数", required = true) @ModelAttribute UserQueryDTO userQueryDTO) {
        // 调用Service层方法进行分页查询
        Page<UserVO> userPage = userService.getUserPage(userQueryDTO);
        return Result.success(userPage);
    }

    /**
     * 获取所有用户列表
     *
     * @return 用户列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取所有用户列表", description = "获取系统中所有用户的列表")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<List<UserVO>> getUserList() {
        List<User> users = userService.list();
        List<UserVO> userVOList = userService.convertToVOList(users);
        return Result.success(userVOList);
    }

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户的详细信息")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<UserVO> getUserById(@Parameter(name = "id", description = "用户ID", required = true) @PathVariable Long id) {
        User user = userService.getById(id);
        UserVO userVO = userService.convertToVO(user);
        return Result.success(userVO);
    }

    /**
     * 新增用户
     *
     * @param userAddDTO 用户信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "新增用户", description = "创建新的用户")
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<?> addUser(@Parameter(name = "userAddDTO", description = "用户信息", required = true) @Valid @RequestBody UserAddDTO userAddDTO) {
        userService.addUser(userAddDTO);
        return Result.success("新增用户成功");
    }

    /**
     * 修改用户
     *
     * @param updateUserDTO 更新用户信息
     * @return 操作结果
     */
    @PostMapping("/update")
    @Operation(summary = "修改用户", description = "更新用户信息")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<?> updateUser(@Parameter(name = "updateUserDTO", description = "更新用户信息", required = true) @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        userService.updateUser(updateUserDTO);
        return Result.success("修改用户成功");
    }

    /**
     * 删除用户
     *
     * @param deleteUserDTO 删除用户信息
     * @return 操作结果
     */
    @PostMapping("/delete")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public Result<?> deleteUser(@Parameter(name = "deleteUserDTO", description = "删除用户信息", required = true) @Valid @RequestBody DeleteUserDTO deleteUserDTO) {
        userService.removeById(deleteUserDTO.getId());
        return Result.success("删除用户成功");
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     * @return 操作结果
     */
    @PostMapping("/delete/batch")
    @Operation(summary = "批量删除用户", description = "根据用户ID列表批量删除用户")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public Result<?> batchDeleteUser(@Parameter(name = "ids", description = "用户ID列表", required = true) @RequestBody List<Long> ids) {
        userService.removeByIds(ids);
        return Result.success("批量删除用户成功");
    }

    /**
     * 获取用户的角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    @GetMapping("/role/{userId}")
    @Operation(summary = "获取用户角色ID列表", description = "根据用户ID获取用户的角色ID列表")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<List<Long>> getUserRoleIds(@Parameter(name = "userId", description = "用户ID", required = true) @PathVariable Long userId) {
        List<Long> roleIds = userService.getUserRoleIds(userId);
        return Result.success(roleIds);
    }

    /**
     * 保存用户的角色关联
     *
     * @param userRoleDTO 用户角色关联信息（包含用户ID和角色ID列表）
     * @return 操作结果
     */
    @PostMapping("/role")
    @Operation(summary = "保存用户角色关联", description = "保存用户与角色的关联关系")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<?> saveUserRoles(
            @Parameter(name = "userRoleDTO", description = "用户角色关联信息", required = true) @Valid @RequestBody UserRoleDTO userRoleDTO) {
        userService.saveUserRoles(userRoleDTO.getUserId(), userRoleDTO.getRoleIds());
        return Result.success("保存用户角色成功");
    }


    /**
     * 重置用户密码
     *
     * @param resetPasswordDTO 重置密码请求参数（包含用户ID和新密码）
     * @return 操作结果
     */
    @PostMapping("/resetPassword")
    @Operation(summary = "重置用户密码", description = "重置用户的密码")
    @PreAuthorize("hasAuthority('system:user:reset')")
    public Result<?> resetPassword(
            @Parameter(name = "resetPasswordDTO", description = "重置密码请求参数", required = true) @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        boolean result = userService.resetPassword(resetPasswordDTO.getUserId(), resetPasswordDTO.getPassword());
        if (result) {
            return Result.success("重置密码成功");
        } else {
            return Result.error("重置密码失败");
        }
    }

    /**
     * 修改用户密码
     *
     * @param userChangePasswordDTO 修改密码请求参数（包含用户ID、旧密码和新密码）
     * @return 操作结果
     */
    @PostMapping("/changePassword")
    @Operation(summary = "修改用户密码", description = "修改用户的密码")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<?> changePassword(
            @Parameter(name = "userChangePasswordDTO", description = "修改密码请求参数", required = true) @Valid @RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        boolean result = userService.changePassword(userChangePasswordDTO.getUserId(), userChangePasswordDTO.getOldPassword(), userChangePasswordDTO.getNewPassword());
        if (result) {
            return Result.success("修改密码成功");
        } else {
            return Result.error("修改密码失败，旧密码错误");
        }
    }

}
