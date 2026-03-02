package com.cobazaar.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录日志VO
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-14
 */
@Data
@Schema(name = "LoginLogVO", description = "登录日志VO")
public class LoginLogVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "登录IP")
    private String ip;

    @Schema(description = "登录地址")
    private String location;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "登录状态：0-失败 1-成功")
    private Integer status;

    @Schema(description = "登录消息")
    private String message;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;
}
