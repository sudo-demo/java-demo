package com.cobazaar.common.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用用户详情实现类
 * 提供标准的Spring Security用户详情实现
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-06
 */
@Data
public class CommonUserDetails implements UserDetails {
    
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 是否账户未过期
     */
    private boolean accountNonExpired = true;

    /**
     * 是否账户未锁定
     */
    private boolean accountNonLocked = true;

    /**
     * 是否凭证未过期
     */
    private boolean credentialsNonExpired = true;

    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 角色列表
     */
    private List<String> roles;

    public CommonUserDetails() {
    }

    public CommonUserDetails(Long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public CommonUserDetails(Long userId, String username, String password,
                           List<String> permissions, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.permissions = permissions;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 合并权限和角色
        List<String> allPermissions = permissions != null ? permissions : new java.util.ArrayList<>();
        List<String> allRoles = roles != null ? roles : new java.util.ArrayList<>();

        return allPermissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 检查是否拥有指定权限
     *
     * @param permission 权限标识
     * @return 是否拥有权限
     */
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }

    /**
     * 检查是否拥有指定角色
     *
     * @param role 角色标识
     * @return 是否拥有角色
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    /**
     * 设置权限列表
     * 注意：此方法仅用于内部测试，实际权限应通过构造函数设置
     *
     * @param authorities 权限列表
     */
    public void setAuthorities(java.util.List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities) {
        // 转换为字符串列表
        if (authorities != null) {
            this.permissions = new java.util.ArrayList<>();
            for (org.springframework.security.core.authority.SimpleGrantedAuthority authority : authorities) {
                this.permissions.add(authority.getAuthority());
            }
        }
    }
}