package com.cobazaar.common.dto;

import lombok.Data;

/**
 * 查询DTO基类
 * 包含分页和排序参数
 * 所有列表查询DTO都应继承此类
 */
@Data
public class QueryDTO {
    /**
     * 页码，默认值为1
     */
    private Integer page = 1;
    
    /**
     * 每页大小，默认值为10
     */
    private Integer pageSize = 10;
    
    /**
     * 排序字段
     */
    private String sortField;
    
    /**
     * 排序方向：asc/desc，默认值为asc
     */
    private String sortOrder = "asc";
}
