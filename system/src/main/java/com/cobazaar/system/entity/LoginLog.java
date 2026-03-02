package com.cobazaar.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 登录日志实体类
 * 对应数据库表 sys_login_log
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Schema(name = "LoginLog", description = "登录日志实体类")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_login_log")
public class LoginLog extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 登录IP
     */
    @TableField("ip_addr")
    @Schema(description = "登录IP", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ip;

    /**
     * 登录地址
     */
    @TableField("login_location")
    @Schema(description = "登录地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String location;

    /**
     * 浏览器
     */
    @Schema(description = "浏览器", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String browser;

    /**
     * 操作系统
     */
    @Schema(description = "操作系统", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String os;

    /**
     * 登录状态:0-成功 1-失败
     */
    @Schema(description = "登录状态:0-成功 1-失败", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 登录消息
     */
    @TableField("msg")
    @Schema(description = "登录消息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String message;

    /**
     * 登录时间
     */
    @TableField("login_time")
    @Schema(description = "登录时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime loginTime;
}
