package com.cobazaar.common.config;

import com.cobazaar.common.security.CommonAccessDeniedHandler;
import com.cobazaar.common.security.CommonAuthenticationEntryPoint;
import com.cobazaar.common.security.JwtAuthenticationFilter;
import com.cobazaar.common.utils.JwtUtils;
import com.cobazaar.common.utils.RedisUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用安全配置类
 * 提供基础的安全配置，业务模块可以通过定义自己的 SecurityFilterChain 来覆盖此配置
 *
 * @author cobazaar
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CommonSecurityConfig {

    private final CommonAuthenticationEntryPoint authenticationEntryPoint;
    private final CommonAccessDeniedHandler accessDeniedHandler;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;
    private final SecurityConfigProperties securityConfigProperties;

    public CommonSecurityConfig(CommonAuthenticationEntryPoint authenticationEntryPoint,
            CommonAccessDeniedHandler accessDeniedHandler,
            JwtUtils jwtUtils,
            RedisUtils redisUtils,
            SecurityConfigProperties securityConfigProperties) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
        this.securityConfigProperties = securityConfigProperties;
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 公共接口放行 - 使用配置的白名单路径
                .antMatchers(getWhitelistPaths()).permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
                .and()
                // 异常处理
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                // 添加JWT认证过滤器
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 获取白名单路径
     * 合并配置文件中的白名单和默认白名单
     *
     * @return 白名单路径数组
     */
    private String[] getWhitelistPaths() {
        List<String> paths = new ArrayList<>();

        // 1. 从配置文件获取 (来自 application-common.yml 和各服务的 application.yml)
        List<String> configPaths = securityConfigProperties.getAllWhitelistPaths();
        if (configPaths != null && !configPaths.isEmpty()) {
            paths.addAll(configPaths);
        }

        // 2. 默认核心白名单 (兜底方案，确保基本功能可用)
        paths.addAll(Arrays.asList(
                "/doc.html",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/actuator/**",
                "/test/**"));

        return paths.toArray(new String[0]);
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, redisUtils);
    }
}