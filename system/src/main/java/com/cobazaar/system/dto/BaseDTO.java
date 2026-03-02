package com.cobazaar.system.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统模块基础DTO
 * 所有DTO应继承此类
 *
 * @author cobazaar
 */
@Data
public class BaseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 基础DTO可以包含通用的分页、排序等字段
    // 具体业务DTO可根据需要继承并扩展
}