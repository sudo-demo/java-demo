package com.cobazaar.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cobazaar.system.entity.LoginLog;

import java.util.List;

/**
 * 登录日志服务接口
 * 提供登录日志相关的业务逻辑操作方法
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
public interface LoginLogService extends IService<LoginLog> {

    /**
     * 记录登录日志
     *
     * @param username 用户名
     * @param ip       登录IP
     * @param status   登录状态：0-失败 1-成功
     * @param message  登录消息
     * @return 登录日志ID
     */
    Long recordLoginLog(String username, String ip, Integer status, String message);

    /**
     * 清理过期的登录日志
     *
     * @param days 保留天数
     * @return 删除的记录数
     */
    int cleanExpiredLogs(Integer days);

    /**
     * 获取登录统计信息
     *
     * @param days 统计天数
     * @return 统计结果
     */
    List<com.cobazaar.system.vo.LoginLogVO> getLoginStatistics(Integer days);

    /**
     * 分页查询登录日志
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.cobazaar.system.vo.LoginLogVO> getLoginLogPage(com.cobazaar.system.dto.LoginLogQueryDTO queryDTO);

    /**
     * 根据ID获取登录日志详情
     *
     * @param id 登录日志ID
     * @return 登录日志详情
     */
    com.cobazaar.system.vo.LoginLogVO getLoginLogById(Long id);
}
