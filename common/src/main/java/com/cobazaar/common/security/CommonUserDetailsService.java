package com.cobazaar.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 通用用户详情服务接口
 * 业务模块需要实现此接口提供具体的用户信息加载逻辑
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-06
 */
public interface CommonUserDetailsService extends UserDetailsService {
    
    /**
     * 根据用户名加载用户详情
     * 业务模块必须实现此方法
     *
     * @param username 用户名
     * @return 用户详情
     * @throws UsernameNotFoundException 用户不存在时抛出
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    
    /**
     * 根据用户ID加载用户详情
     *
     * @param userId 用户ID
     * @return 用户详情
     * @throws UsernameNotFoundException 用户不存在时抛出
     */
    UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException;
    
    /**
     * 检查用户是否启用
     *
     * @param username 用户名
     * @return 是否启用
     */
    boolean isUserEnabled(String username);
    
    /**
     * 检查用户是否锁定
     *
     * @param username 用户名
     * @return 是否锁定
     */
    boolean isUserLocked(String username);
    
    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    java.util.List<String> getUserPermissions(Long userId);
}