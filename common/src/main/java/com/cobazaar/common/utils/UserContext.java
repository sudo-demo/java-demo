package com.cobazaar.common.utils;

import com.cobazaar.common.vo.UserInfoVO;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 用户上下文工具类
 * 用于获取当前登录用户的信息
 *
 * @author cobazaar
 */
public class UserContext {

    /**
     * ThreadLocal存储用户信息
     */
    private static final ThreadLocal<UserInfoVO> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 获取当前用户信息
     *
     * @return 用户详细信息
     */
    public static UserInfoVO getUser() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 设置用户信息到ThreadLocal
     *
     * @param userInfoVO 用户详细信息
     */
    public static void setUser(UserInfoVO userInfoVO) {
        if (userInfoVO != null) {
            USER_THREAD_LOCAL.set(userInfoVO);
        } else {
            USER_THREAD_LOCAL.remove();
        }
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID
     */
    public static Long getUserId() {
        return Optional.ofNullable(getUser()).map(UserInfoVO::getId).orElse(null);
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名
     */
    public static String getUsername() {
        return Optional.ofNullable(getUser()).map(UserInfoVO::getUsername).orElse(null);
    }

    /**
     * 获取当前用户角色列表
     *
     * @return 角色列表
     */
    public static List<String> getRoles() {
        return Optional.ofNullable(getUser()).map(UserInfoVO::getRoles).orElse(Collections.emptyList());
    }

    /**
     * 获取当前用户权限列表
     *
     * @return 权限列表
     */
    public static List<String> getPermissions() {
        return Optional.ofNullable(getUser()).map(UserInfoVO::getPermissions).orElse(Collections.emptyList());
    }

    /**
     * 检查当前用户是否拥有指定权限
     *
     * @param permission 权限字符串
     * @return 是否拥有权限
     */
    public static boolean hasPermission(String permission) {
        List<String> permissions = getPermissions();
        if (permissions.isEmpty()) {
            return false;
        }
        // 检查是否拥有所有权限（*）
        if (permissions.contains("*")) {
            return true;
        }
        // 检查是否拥有指定权限
        return permissions.contains(permission);
    }

    /**
     * 检查当前用户是否拥有指定角色
     *
     * @param role 角色字符串
     * @return 是否拥有角色
     */
    public static boolean hasRole(String role) {
        List<String> roles = getRoles();
        if (roles.isEmpty()) {
            return false;
        }
        return roles.contains(role);
    }

    /**
     * 检查用户是否已登录
     *
     * @return 是否已登录
     */
    public static boolean isLoggedIn() {
        return getUser() != null;
    }

    /**
     * 清除ThreadLocal中的用户信息
     * 防止内存泄漏
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}