package com.cobazaar.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置类
 * 提供分页插件等核心功能配置
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-05
 */
@Slf4j
@Configuration
@MapperScan("com.cobazaar.**.mapper")
public class MybatisPlusConfig {

    /**
     * MyBatis Plus拦截器配置
     * 主要配置分页插件
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 添加分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置数据库类型
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求
        paginationInnerInterceptor.setOverflow(false);
        // 单页分页条数限制，默认无限制
        paginationInnerInterceptor.setMaxLimit(1000L);
        
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        
        log.info("MyBatis Plus分页插件配置完成");
        return interceptor;
    }
}