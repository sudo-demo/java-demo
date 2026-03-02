package com.cobazaar.common.security;

import com.cobazaar.common.enums.RedisModulePrefix;
import com.cobazaar.common.utils.JwtUtils;
import com.cobazaar.common.utils.RedisUtils;
import com.cobazaar.common.utils.UserContext;
import com.cobazaar.common.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 * 提供JWT token验证和Redis用户信息获取功能
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-06
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;

    /**
     * JWT密钥
     */
    @Value("${jwt.secret:default_secret_key_12345}")
    private String jwtSecret;

    /**
     * JWT请求头名称
     */
    @Value("${jwt.header:Authorization}")
    private String jwtHeader;

    /**
     * JWT请求头前缀
     */
    @Value("${jwt.prefix:Bearer}")
    private String jwtPrefix;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, RedisUtils redisUtils) {
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token)) {
            try {
                // 1. 检查Token是否在黑名单中
                String blacklistKey = RedisModulePrefix.TOKEN_BLACKLIST.addPrefix(token);
                if (redisUtils.hasKey(blacklistKey)) {
                    log.warn("Token已在黑名单中: {}", token);
                    // 可以选择直接返回401，这里让Security框架处理（不设置Authentication）
                    filterChain.doFilter(request, response);
                    return;
                }

                // 2. 验证Token是否有效
                if (jwtUtils.validateToken(token, jwtSecret)) {
                    String username = jwtUtils.getUsernameFromToken(token, jwtSecret);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // 3. 从Redis获取用户信息
                        String userKey = RedisModulePrefix.USER.addPrefix(username);
                        UserInfoVO userInfoVO = redisUtils.get(userKey, UserInfoVO.class);

                        if (userInfoVO != null) {
                            // 4. 设置UserContext
                            UserContext.setUser(userInfoVO);

                            // 5. 构建UserDetails
                            UserDetails userDetails = new User(
                                    userInfoVO.getUsername(),
                                    "", // 密码置空
                                    userInfoVO.getStatus() == 1,
                                    true,
                                    true,
                                    true,
                                    Collections.emptyList() // 权限列表可从userInfoVO获取并转换
                            );

                            // 6. 设置SecurityContext
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.debug("用户认证成功: {}", username);
                        } else {
                            log.warn("Redis中未找到用户信息: {}", username);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("JWT认证过程发生异常", e);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 7. 清理UserContext，防止内存泄漏
            UserContext.clear();
        }
    }

    /**
     * 从请求中提取token
     *
     * @param request HTTP请求
     * @return token字符串
     */
    protected String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtPrefix + " ")) {
            return bearerToken.substring((jwtPrefix + " ").length());
        }
        return null;
    }
}