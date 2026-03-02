package com.cobazaar.common.vo;

import com.cobazaar.common.annotation.DataMasking;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息VO
 * 包含用户基本信息、角色、权限等详细信息
 *
 * @author cobazaar
 */
@Data
@Schema(name = "UserInfoVO", description = "用户详细信息")
public class UserInfoVO {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long id;



//    /**
//     * 密码
//     */
//    @Schema(description = "密码")
//    private String password;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    @DataMasking(type = DataMasking.MaskingType.NAME)
    private String username;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    @DataMasking(type = DataMasking.MaskingType.NAME)
    private String realName;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @DataMasking(type = DataMasking.MaskingType.MOBILE)
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @DataMasking(type = DataMasking.MaskingType.EMAIL)
    private String email;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;

    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 用户角色列表
     */
    @Schema(description = "用户角色列表")
    private List<String> roles;

    /**
     * 用户权限列表
     */
    @Schema(description = "用户权限列表")
    private List<String> permissions;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long departmentId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String departmentName;
}