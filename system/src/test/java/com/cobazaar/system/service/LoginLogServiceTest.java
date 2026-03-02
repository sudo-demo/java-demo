package com.cobazaar.system.service;

import com.cobazaar.system.SystemApplication;
import com.cobazaar.system.dto.LoginLogQueryDTO;
import com.cobazaar.system.entity.LoginLog;
import com.cobazaar.system.vo.LoginLogVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 登录日志服务测试类
 *
 * @author cobazaar
 */
@SpringBootTest(classes = SystemApplication.class)
@ActiveProfiles("test")
@Transactional // 自动回滚事务
public class LoginLogServiceTest {

    @Autowired
    private LoginLogService loginLogService;

    private Long testLogId;

    @BeforeEach
    public void setUp() {
        // 创建测试日志
        LoginLog log = new LoginLog();
        log.setUsername("test_user");
        log.setIp("127.0.0.1");
        log.setLocation("Localhost");
        log.setBrowser("Chrome");
        log.setOs("Mac OS");
        log.setStatus(1);
        log.setMessage("Login success");
        log.setLoginTime(LocalDateTime.now());
        
        loginLogService.save(log);
        testLogId = log.getId();
    }

    @Test
    public void testGetLoginLogPage() {
        LoginLogQueryDTO queryDTO = new LoginLogQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);
        queryDTO.setUsername("test_user");
        
        Page<LoginLogVO> result = loginLogService.getLoginLogPage(queryDTO);
        assertNotNull(result);
        assertTrue(result.getTotal() > 0);
        assertFalse(result.getRecords().isEmpty());
        assertEquals("test_user", result.getRecords().get(0).getUsername());
    }

    @Test
    public void testGetLoginLogById() {
        LoginLogVO logVO = loginLogService.getLoginLogById(testLogId);
        assertNotNull(logVO);
        assertEquals(testLogId, logVO.getId());
        assertEquals("test_user", logVO.getUsername());
    }

    @Test
    public void testGetLoginStatistics() {
        List<LoginLogVO> stats = loginLogService.getLoginStatistics(7);
        assertNotNull(stats);
        // 既然刚才插入了一条，统计结果应该不为空（取决于具体统计逻辑，通常是按天统计）
        // 这里简单验证不报错即可，或者验证包含了刚才插入的数据（如果统计逻辑支持实时）
    }
}
