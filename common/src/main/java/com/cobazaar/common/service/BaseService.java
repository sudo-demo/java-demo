package com.cobazaar.common.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 通用Service接口
 * 继承MyBatis Plus的IService，提供基础的CRUD操作
 *
 * @param <T> 实体类型
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-05
 */
public interface BaseService<T> extends IService<T> {
    
    /**
     * 根据ID逻辑删除
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean logicRemoveById(Long id);
    
    /**
     * 批量逻辑删除
     *
     * @param ids 主键ID集合
     * @return 是否删除成功
     */
    boolean logicRemoveBatchByIds(Iterable<Long> ids);
}