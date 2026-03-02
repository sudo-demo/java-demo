-- 创建 demo 数据库
CREATE DATABASE IF NOT EXISTS `demo` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 使用 demo 数据库
USE `demo`;

-- 创建用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` bigint NOT NULL COMMENT '主键ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
    `status` tinyint DEFAULT '0' COMMENT '状态（0-正常，1-禁用）',
    `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
    `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
    `del_flag` tinyint DEFAULT '0' COMMENT '删除标识（0-正常，1-删除）',
    `version` int DEFAULT '1' COMMENT '版本号（乐观锁）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`),
    KEY `idx_phone` (`phone`),
    KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 创建角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` bigint NOT NULL COMMENT '主键ID',
    `role_name` varchar(50) NOT NULL COMMENT '角色名称',
    `role_key` varchar(50) NOT NULL COMMENT '角色权限字符串',
    `role_sort` int DEFAULT '0' COMMENT '显示顺序',
    `status` tinyint DEFAULT '0' COMMENT '角色状态（0-正常，1-停用）',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
    `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
    `del_flag` tinyint DEFAULT '0' COMMENT '删除标识（0-正常，1-删除）',
    `version` int DEFAULT '1' COMMENT '版本号（乐观锁）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 创建菜单表
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` bigint NOT NULL COMMENT '主键ID',
    `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
    `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
    `order_num` int DEFAULT '0' COMMENT '显示顺序',
    `path` varchar(200) DEFAULT '' COMMENT '路由地址',
    `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
    `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
    `is_frame` tinyint DEFAULT '1' COMMENT '是否为外链（0是 1否）',
    `is_cache` tinyint DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
    `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
    `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
    `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
    `remark` varchar(500) DEFAULT '' COMMENT '备注',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
    `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
    `del_flag` tinyint DEFAULT '0' COMMENT '删除标识（0-正常，1-删除）',
    `version` int DEFAULT '1' COMMENT '版本号（乐观锁）',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户和角色关联表';

-- 创建角色菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和菜单关联表';

-- 插入初始数据
-- 插入管理员用户
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `phone`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb0bta/OauRxaOKSr.QhqyD2R5FKvMQjmHoLkm5Sy', '管理员', 'admin@demo.com', '13800138000', 0),
(2, 'test', '$2a$10$N.zmdr9k7uOCQb0bta/OauRxaOKSr.QhqyD2R5FKvMQjmHoLkm5Sy', '测试用户', 'test@demo.com', '13800138001', 0);

-- 插入角色数据
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `role_sort`, `status`, `remark`) VALUES
(1, '超级管理员', 'admin', 1, 0, '超级管理员'),
(2, '普通用户', 'user', 2, 0, '普通用户');

-- 插入菜单数据（简化版）
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `status`, `perms`, `icon`) VALUES
(4, '菜单管理', 1, 3, 'menu', 'system/menu/index', 'C', '0', 'system:menu:list', 'tree-table'),
(5, '部门管理', 1, 4, 'dept', 'system/dept/index', 'C', '0', 'system:dept:list', 'tree'),
(6, '登录日志', 1, 5, 'loginlog', 'system/loginlog/index', 'C', '0', 'system:loginlog:list', 'log'),
(100, '用户查询', 2, 1, '', '', 'F', '0', 'system:user:query', '#'),
(101, '用户新增', 2, 2, '', '', 'F', '0', 'system:user:add', '#'),
(102, '用户修改', 2, 3, '', '', 'F', '0', 'system:user:edit', '#'),
(103, '用户删除', 2, 4, '', '', 'F', '0', 'system:user:remove', '#'),
(104, '重置密码', 2, 5, '', '', 'F', '0', 'system:user:reset', '#'),
(110, '角色查询', 3, 1, '', '', 'F', '0', 'system:role:query', '#'),
(111, '角色新增', 3, 2, '', '', 'F', '0', 'system:role:add', '#'),
(112, '角色修改', 3, 3, '', '', 'F', '0', 'system:role:edit', '#'),
(113, '角色删除', 3, 4, '', '', 'F', '0', 'system:role:remove', '#'),
(120, '菜单查询', 4, 1, '', '', 'F', '0', 'system:menu:query', '#'),
(121, '菜单新增', 4, 2, '', '', 'F', '0', 'system:menu:add', '#'),
(122, '菜单修改', 4, 3, '', '', 'F', '0', 'system:menu:edit', '#'),
(123, '菜单删除', 4, 4, '', '', 'F', '0', 'system:menu:remove', '#'),
(130, '部门查询', 5, 1, '', '', 'F', '0', 'system:dept:query', '#'),
(131, '部门新增', 5, 2, '', '', 'F', '0', 'system:dept:add', '#'),
(132, '部门修改', 5, 3, '', '', 'F', '0', 'system:dept:edit', '#'),
(133, '部门删除', 5, 4, '', '', 'F', '0', 'system:dept:remove', '#'),
(140, '日志查询', 6, 1, '', '', 'F', '0', 'system:loginlog:query', '#'),
(141, '日志删除', 6, 2, '', '', 'F', '0', 'system:loginlog:remove', '#');

-- 插入用户角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- 插入角色菜单关联
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(1, 100), (1, 101), (1, 102), (1, 103), (1, 104),
(1, 110), (1, 111), (1, 112), (1, 113),
(1, 120), (1, 121), (1, 122), (1, 123),
(1, 130), (1, 131), (1, 132), (1, 133),
(1, 140), (1, 141),
(2, 2);

-- 查询验证
SELECT 'Database demo created successfully!' AS message;
SELECT COUNT(*) as user_count FROM sys_user;
SELECT COUNT(*) as role_count FROM sys_role;
SELECT COUNT(*) as menu_count FROM sys_menu;