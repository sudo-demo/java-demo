package com.cobazaar.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cobazaar.common.result.Result;
import com.cobazaar.system.entity.LoginLog;
import com.cobazaar.system.service.LoginLogService;
import com.cobazaar.system.vo.LoginLogVO;
import com.cobazaar.system.dto.LoginLogQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录日志管理控制器
 * 提供登录日志相关的RESTful API接口
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Tag(name = "登录日志管理")
@RestController
@RequestMapping("/login-log")
@RequiredArgsConstructor
public class LoginLogController {

    private final LoginLogService loginLogService;

    /**
     * 获取登录日志列表（分页）
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param username 用户名（可选）
     * @param status   登录状态（可选）
     * @return 登录日志列表
     */
    @GetMapping("/page")
    @Operation(summary = "获取登录日志列表（分页）", description = "根据查询参数获取登录日志列表，支持分页")
    @PreAuthorize("hasAuthority('system:loginlog:list')")
    public Result<Page<LoginLogVO>> getLoginLogPage(
            @Parameter(name = "page", description = "页码", schema = @Schema(defaultValue = "1")) @RequestParam(defaultValue = "1") Integer page,
            @Parameter(name = "pageSize", description = "每页大小", schema = @Schema(defaultValue = "10")) @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(name = "username", description = "用户名（可选）") @RequestParam(required = false) String username,
            @Parameter(name = "status", description = "登录状态（可选）") @RequestParam(required = false) Integer status) {
        LoginLogQueryDTO queryDTO = new LoginLogQueryDTO();
        queryDTO.setPage(page);
        queryDTO.setPageSize(pageSize);
        queryDTO.setUsername(username);
        queryDTO.setStatus(status);
        Page<LoginLogVO> loginLogPage = loginLogService.getLoginLogPage(queryDTO);
        return Result.success(loginLogPage);
    }

    /**
     * 获取登录日志详情
     *
     * @param id 登录日志ID
     * @return 登录日志详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取登录日志详情", description = "根据登录日志ID获取登录日志的详细信息")
    @PreAuthorize("hasAuthority('system:loginlog:query')")
    public Result<LoginLogVO> getLoginLogById(@Parameter(name = "id", description = "登录日志ID", required = true) @PathVariable Long id) {
        LoginLogVO loginLog = loginLogService.getLoginLogById(id);
        return Result.success(loginLog);
    }

    /**
     * 清理过期的登录日志
     *
     * @param days 保留天数
     * @return 操作结果
     */
    @PostMapping("/clean")
    @Operation(summary = "清理过期登录日志", description = "清理指定天数之前的登录日志")
    @PreAuthorize("hasAuthority('system:loginlog:remove')")
    public Result<?> cleanExpiredLogs(@Parameter(name = "days", description = "保留天数", schema = @Schema(defaultValue = "30")) @RequestParam(defaultValue = "30") Integer days) {
        int count = loginLogService.cleanExpiredLogs(days);
        return Result.success("清理过期登录日志成功，共删除 " + count + " 条记录");
    }

    /**
     * 获取登录统计信息
     *
     * @param days 统计天数
     * @return 统计结果
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取登录统计信息", description = "获取指定天数的登录统计信息")
    @PreAuthorize("hasAuthority('system:loginlog:list')")
    public Result<List<LoginLogVO>> getLoginStatistics(@Parameter(name = "days", description = "统计天数", schema = @Schema(defaultValue = "7")) @RequestParam(defaultValue = "7") Integer days) {
        List<LoginLogVO> statistics = loginLogService.getLoginStatistics(days);
        return Result.success(statistics);
    }

    /**
     * 删除登录日志
     *
     * @param id 登录日志ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "删除登录日志", description = "根据登录日志ID删除登录日志")
    @PreAuthorize("hasAuthority('system:loginlog:remove')")
    public Result<?> deleteLoginLog(@Parameter(name = "id", description = "登录日志ID", required = true) @PathVariable Long id) {
        loginLogService.removeById(id);
        return Result.success("删除登录日志成功");
    }

    /**
     * 批量删除登录日志
     *
     * @param ids 登录日志ID列表
     * @return 操作结果
     */
    @PostMapping("/batch")
    @Operation(summary = "批量删除登录日志", description = "根据登录日志ID列表批量删除登录日志")
    @PreAuthorize("hasAuthority('system:loginlog:remove')")
    public Result<?> batchDeleteLoginLog(@Parameter(name = "ids", description = "登录日志ID列表", required = true) @RequestBody List<Long> ids) {
        loginLogService.removeByIds(ids);
        return Result.success("批量删除登录日志成功");
    }
}
