package com.cobazaar.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 元对象处理器
 * 自动填充创建时间、更新时间等字段
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-05
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充...");
        
        // 创建时间和更新时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        
        // 删除标识默认为0（未删除）
        this.strictInsertFill(metaObject, "delFlag", Integer.class, 0);
        
        // 版本号默认为1
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
        
        // TODO: 从上下文中获取当前用户ID
        // this.strictInsertFill(metaObject, "createBy", Long.class, getCurrentUserId());
        // this.strictInsertFill(metaObject, "updateBy", Long.class, getCurrentUserId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新填充...");
        
        // 只更新更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        
        // TODO: 从上下文中获取当前用户ID
        // this.strictUpdateFill(metaObject, "updateBy", Long.class, getCurrentUserId());
    }

    /**
     * 获取当前用户ID
     * TODO: 需要结合实际的用户认证体系实现
     *
     * @return 当前用户ID
     */
    private Long getCurrentUserId() {
        // 这里可以根据实际的用户认证方式来获取用户ID
        // 例如从Spring Security、Shiro或者自定义的用户上下文中获取
        return 1L; // 临时返回默认值
    }
}