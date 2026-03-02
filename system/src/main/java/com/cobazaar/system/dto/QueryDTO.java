package com.cobazaar.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 查询DTO
 * 用于分页查询的基础参数
 *
 * @author cobazaar
 */
@Schema(name = "QueryDTO", description = "查询DTO")
@Data
public class QueryDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderBy;

    /**
     * 排序方向：asc/desc
     */
    @Schema(description = "排序方向：asc/desc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderDirection = "desc";
}