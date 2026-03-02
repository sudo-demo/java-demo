package com.cobazaar.common.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限工具类
 * 用于精细化权限管理
 * 
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Slf4j
public final class PermissionUtils {

    private PermissionUtils() {
        throw new UnsupportedOperationException("PermissionUtils is a utility class and cannot be instantiated");
    }

    // ==================== 权限检查 ====================

    /**
     * 检查用户是否有指定权限
     * 
     * @param userPermissions 用户权限列表
     * @param permission       要检查的权限
     * @return 是否有权限
     */
    public static boolean hasPermission(List<String> userPermissions, String permission) {
        if (StrUtil.isBlank(permission)) {
            return true;
        }
        if (userPermissions == null || userPermissions.isEmpty()) {
            return false;
        }
        return userPermissions.contains(permission);
    }

    /**
     * 检查用户是否有指定权限（支持通配符）
     * 
     * @param userPermissions 用户权限列表
     * @param permission       要检查的权限
     * @return 是否有权限
     */
    public static boolean hasPermissionWithWildcard(List<String> userPermissions, String permission) {
        if (StrUtil.isBlank(permission)) {
            return true;
        }
        if (userPermissions == null || userPermissions.isEmpty()) {
            return false;
        }

        // 直接匹配
        if (userPermissions.contains(permission)) {
            return true;
        }

        // 通配符匹配
        String wildcardPermission = permission.replaceAll("\\*", ".*");
        for (String userPerm : userPermissions) {
            if (userPerm.equals("*") || userPerm.matches(wildcardPermission)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查用户是否有任意一个指定权限
     * 
     * @param userPermissions 用户权限列表
     * @param permissions     要检查的权限列表
     * @return 是否有任意一个权限
     */
    public static boolean hasAnyPermission(List<String> userPermissions, List<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return true;
        }
        if (userPermissions == null || userPermissions.isEmpty()) {
            return false;
        }
        return permissions.stream().anyMatch(perm -> hasPermission(userPermissions, perm));
    }

    /**
     * 检查用户是否有所有指定权限
     * 
     * @param userPermissions 用户权限列表
     * @param permissions     要检查的权限列表
     * @return 是否有所有权限
     */
    public static boolean hasAllPermissions(List<String> userPermissions, List<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return true;
        }
        if (userPermissions == null || userPermissions.isEmpty()) {
            return false;
        }
        return permissions.stream().allMatch(perm -> hasPermission(userPermissions, perm));
    }

    // ==================== 角色检查 ====================

    /**
     * 检查用户是否有指定角色
     * 
     * @param userRoles 用户角色列表
     * @param role      要检查的角色
     * @return 是否有角色
     */
    public static boolean hasRole(List<String> userRoles, String role) {
        if (StrUtil.isBlank(role)) {
            return true;
        }
        if (userRoles == null || userRoles.isEmpty()) {
            return false;
        }
        return userRoles.contains(role);
    }

    /**
     * 检查用户是否有任意一个指定角色
     * 
     * @param userRoles 用户角色列表
     * @param roles     要检查的角色列表
     * @return 是否有任意一个角色
     */
    public static boolean hasAnyRole(List<String> userRoles, List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return true;
        }
        if (userRoles == null || userRoles.isEmpty()) {
            return false;
        }
        return roles.stream().anyMatch(role -> hasRole(userRoles, role));
    }

    // ==================== 数据权限 ====================

    /**
     * 检查用户是否有权限访问指定数据
     * 
     * @param userId      用户ID
     * @param dataOwnerId 数据所有者ID
     * @param isAdmin     是否为管理员
     * @return 是否有权限
     */
    public static boolean hasDataPermission(Long userId, Long dataOwnerId, boolean isAdmin) {
        if (isAdmin) {
            return true;
        }
        if (userId == null || dataOwnerId == null) {
            return false;
        }
        return userId.equals(dataOwnerId);
    }

    /**
     * 检查用户是否有权限访问指定部门数据
     * 
     * @param userDeptId   用户部门ID
     * @param dataDeptId   数据部门ID
     * @param deptHierarchy 部门层级关系
     * @param isAdmin      是否为管理员
     * @return 是否有权限
     */
    public static boolean hasDeptPermission(Long userDeptId, Long dataDeptId, List<Long> deptHierarchy, boolean isAdmin) {
        if (isAdmin) {
            return true;
        }
        if (userDeptId == null || dataDeptId == null) {
            return false;
        }
        if (userDeptId.equals(dataDeptId)) {
            return true;
        }
        if (deptHierarchy != null && deptHierarchy.contains(dataDeptId)) {
            return true;
        }
        return false;
    }

    // ==================== 权限处理 ====================

    /**
     * 合并权限列表（去重）
     * 
     * @param permissions1 权限列表1
     * @param permissions2 权限列表2
     * @return 合并后的权限列表
     */
    public static List<String> mergePermissions(List<String> permissions1, List<String> permissions2) {
        Set<String> merged = new java.util.HashSet<>();
        if (permissions1 != null) {
            merged.addAll(permissions1);
        }
        if (permissions2 != null) {
            merged.addAll(permissions2);
        }
        return merged.stream().collect(Collectors.toList());
    }

    /**
     * 过滤权限列表（移除重复和空值）
     * 
     * @param permissions 权限列表
     * @return 过滤后的权限列表
     */
    public static List<String> filterPermissions(List<String> permissions) {
        if (permissions == null) {
            return new java.util.ArrayList<>();
        }
        return permissions.stream()
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 解析权限字符串为列表
     * 
     * @param permissionStr 权限字符串，逗号分隔
     * @return 权限列表
     */
    public static List<String> parsePermissions(String permissionStr) {
        if (StrUtil.isBlank(permissionStr)) {
            return new java.util.ArrayList<>();
        }
        return java.util.Arrays.stream(permissionStr.split(","))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
    }

    /**
     * 将权限列表转换为字符串
     * 
     * @param permissions 权限列表
     * @return 权限字符串，逗号分隔
     */
    public static String formatPermissions(List<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return "";
        }
        return String.join(",", permissions);
    }
}
