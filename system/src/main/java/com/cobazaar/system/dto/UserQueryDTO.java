package com.cobazaar.system.dto;

import com.cobazaar.common.dto.QueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询DTO
 * 用于接收用户列表查询的参数
 *
 * @author cobazaar
 */
@Schema(name = "UserQueryDTO", description = "用户查询DTO")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryDTO extends QueryDTO {
    /**
     * 用户名
     */
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String username;
    
    /**
     * 昵称
     */
    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String nickname;
    
    /**
     * 手机号
     */
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String phone;
    
    /**
     * 邮箱
     */
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String email;
    
    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String realName;
    
    /**
     * 状态:0-禁用 1-启用
     */
    @Schema(description = "状态:0-禁用 1-启用", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;
}
