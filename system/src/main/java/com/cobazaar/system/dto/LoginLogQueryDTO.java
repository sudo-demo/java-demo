package com.cobazaar.system.dto;

import com.cobazaar.common.dto.QueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录日志查询DTO
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "LoginLogQueryDTO", description = "登录日志查询参数")
public class LoginLogQueryDTO extends QueryDTO {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "登录状态")
    private Integer status;
}
