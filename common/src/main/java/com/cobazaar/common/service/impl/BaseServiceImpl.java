package com.cobazaar.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cobazaar.common.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * 通用Service实现类
 * 提供基础的CRUD操作和逻辑删除功能
 *
 * @param <M> Mapper接口
 * @param <T> 实体类型
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-05
 */
public class BaseServiceImpl<M extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>, T> 
        extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean logicRemoveById(Long id) {
        T entity = this.getById(id);
        if (entity == null) {
            return false;
        }
        
        // 这里假设实体类中有delFlag字段，0表示正常，1表示删除
        // 具体实现需要根据实际的实体类结构调整
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean logicRemoveBatchByIds(Iterable<Long> ids) {
        if (ids == null) {
            return false;
        }
        
        return this.removeByIds((Collection<? extends Serializable>) ids);
    }
}