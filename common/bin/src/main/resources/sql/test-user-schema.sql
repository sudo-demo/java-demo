-- 测试用户表
CREATE TABLE IF NOT EXISTS `test_user` (
    `id` bigint NOT NULL COMMENT '主键ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `status` tinyint DEFAULT '0' COMMENT '状态（0-正常，1-禁用）',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
    `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
    `del_flag` tinyint DEFAULT '0' COMMENT '删除标识（0-正常，1-删除）',
    `version` int DEFAULT '1' COMMENT '版本号（乐观锁）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试用户表';

-- 插入测试数据
INSERT INTO `test_user` (`id`, `username`, `password`, `email`, `phone`, `status`) VALUES
(1, 'admin', '$2a$10$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345', 'admin@example.com', '13800138000', 0),
(2, 'test', '$2a$10$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345', 'test@example.com', '13800138001', 0),
(3, 'user', '$2a$10$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345', 'user@example.com', '13800138002', 1);

-- 查询测试
SELECT * FROM test_user WHERE del_flag = 0;