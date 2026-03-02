package com.cobazaar.common.security;

import com.cobazaar.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情服务实现类
 * 从Redis中获取用户信息，不需要数据库查询
 * 其他服务可以直接使用这个实现，不需要实现UserDetailsService接口
 *
 * @author cobazaar
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("从Redis加载用户详情: {}", username);
        
        // 从Redis中获取用户信息，key值就是username
        CommonUserDetails userDetails = (CommonUserDetails) redisUtils.get(username);
        
        if (userDetails == null) {
            log.warn("Redis中未找到用户信息: {}", username);
            // 如果Redis中没有用户信息，创建一个默认的用户详情对象
            userDetails = new CommonUserDetails();
            userDetails.setUsername(username);
            userDetails.setPassword("password"); // 密码在JWT验证中已经验证过了，这里只是为了满足UserDetails接口
            
            // 设置默认角色
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            userDetails.setAuthorities(authorities);
            
            userDetails.setEnabled(true);
            userDetails.setAccountNonExpired(true);
            userDetails.setAccountNonLocked(true);
            userDetails.setCredentialsNonExpired(true);
        }
        
        log.debug("从Redis加载用户详情成功: {}", username);
        return userDetails;
    }

    /**
     * 根据用户ID加载用户详情
     *
     * @param userId 用户ID
     * @return 用户详情
     * @throws UsernameNotFoundException 用户不存在时抛出
     */
    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        log.info("从Redis加载用户详情: userId={}", userId);
        
        // 从Redis中获取用户信息，使用userId作为key
        CommonUserDetails userDetails = (CommonUserDetails) redisUtils.get(userId.toString());
        
        if (userDetails == null) {
            log.warn("Redis中未找到用户信息: userId={}", userId);
            throw new UsernameNotFoundException("User not found: " + userId);
        }
        
        log.debug("从Redis加载用户详情成功: userId={}", userId);
        return userDetails;
    }

    /**
     * 检查用户是否启用
     *
     * @param username 用户名
     * @return 是否启用
     */
    public boolean isUserEnabled(String username) {
        try {
            CommonUserDetails userDetails = (CommonUserDetails) loadUserByUsername(username);
            return userDetails != null && userDetails.isEnabled();
        } catch (Exception e) {
            log.error("检查用户启用状态失败: {}", username, e);
            return false;
        }
    }

    /**
     * 检查用户是否锁定
     *
     * @param username 用户名
     * @return 是否锁定
     */
    public boolean isUserLocked(String username) {
        try {
            CommonUserDetails userDetails = (CommonUserDetails) loadUserByUsername(username);
            return userDetails != null && !userDetails.isAccountNonLocked();
        } catch (Exception e) {
            log.error("检查用户锁定状态失败: {}", username, e);
            return false;
        }
    }

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> getUserPermissions(Long userId) {
        // 从Redis中获取用户权限
        String permissionsKey = "permissions:" + userId;
        List<String> permissions = (List<String>) redisUtils.get(permissionsKey);
        return permissions != null ? permissions : new ArrayList<>();
    }
}
