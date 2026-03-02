package com.cobazaar.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cobazaar.system.entity.LoginLog;
import com.cobazaar.system.mapper.LoginLogMapper;
import com.cobazaar.system.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录日志服务实现类
 * 实现登录日志相关的业务逻辑操作
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public Long recordLoginLog(String username, String ip, Integer status, String message) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setIp(ip);
        // TODO: 这里可以通过IP地址获取登录地址，暂时设为null
        loginLog.setLocation(null);
        // TODO: 这里可以解析用户代理获取浏览器和操作系统信息，暂时设为null
        loginLog.setBrowser(null);
        loginLog.setOs(null);
        loginLog.setStatus(status);
        loginLog.setMessage(message);
        loginLog.setLoginTime(LocalDateTime.now());
        save(loginLog);
        return loginLog.getId();
    }

    @Override
    public int cleanExpiredLogs(Integer days) {
        LocalDateTime expireTime = LocalDateTime.now().minusDays(days);
        LambdaQueryWrapper<LoginLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(LoginLog::getLoginTime, expireTime);
        return loginLogMapper.delete(queryWrapper);
    }

    @Override
    public List<com.cobazaar.system.vo.LoginLogVO> getLoginStatistics(Integer days) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        LambdaQueryWrapper<LoginLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(LoginLog::getLoginTime, startTime);
        queryWrapper.orderByDesc(LoginLog::getLoginTime);
        List<LoginLog> list = list(queryWrapper);
        return convertToVOList(list);
    }

    @Override
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.cobazaar.system.vo.LoginLogVO> getLoginLogPage(com.cobazaar.system.dto.LoginLogQueryDTO queryDTO) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<LoginLog> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        LambdaQueryWrapper<LoginLog> queryWrapper = new LambdaQueryWrapper<>();
        if (queryDTO.getUsername() != null && !queryDTO.getUsername().isEmpty()) {
            queryWrapper.like(LoginLog::getUsername, queryDTO.getUsername());
        }
        if (queryDTO.getStatus() != null) {
            queryWrapper.eq(LoginLog::getStatus, queryDTO.getStatus());
        }
        queryWrapper.orderByDesc(LoginLog::getLoginTime);
        
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<LoginLog> resultPage = page(page, queryWrapper);
        
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.cobazaar.system.vo.LoginLogVO> voPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        voPage.setRecords(convertToVOList(resultPage.getRecords()));
        voPage.setTotal(resultPage.getTotal());
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setPages(resultPage.getPages());
        
        return voPage;
    }

    @Override
    public com.cobazaar.system.vo.LoginLogVO getLoginLogById(Long id) {
        LoginLog loginLog = getById(id);
        return convertToVO(loginLog);
    }

    private com.cobazaar.system.vo.LoginLogVO convertToVO(LoginLog loginLog) {
        if (loginLog == null) {
            return null;
        }
        com.cobazaar.system.vo.LoginLogVO vo = new com.cobazaar.system.vo.LoginLogVO();
        cn.hutool.core.bean.BeanUtil.copyProperties(loginLog, vo);
        return vo;
    }

    private List<com.cobazaar.system.vo.LoginLogVO> convertToVOList(List<LoginLog> list) {
        if (list == null || list.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return list.stream().map(this::convertToVO).collect(java.util.stream.Collectors.toList());
    }
}
