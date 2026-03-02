#!/bin/bash

# 完善版数据库初始化脚本 - 支持第三方认证和登录限制
echo "🐳 初始化支持第三方认证的 demo 数据库..."
echo "==============================================="

# 使用您提供的配置
CONTAINER_NAME="mysql8"
MYSQL_ROOT_PASSWORD="root"
MYSQL_PORT="3307"
MYSQL_HOST="127.0.0.1"
DATABASE_NAME="demo"

echo "📋 配置信息："
echo "   容器名: $CONTAINER_NAME"
echo "   主机: $MYSQL_HOST"
echo "   端口: $MYSQL_PORT"
echo "   密码: $MYSQL_ROOT_PASSWORD"
echo "   数据库: $DATABASE_NAME"
echo ""

# 检查容器状态
echo "🔍 检查 MySQL 容器状态..."
if ! docker ps | grep -q ${CONTAINER_NAME}; then
    echo "❌ 容器 ${CONTAINER_NAME} 未运行"
    echo "💡 请先启动您的 MySQL 容器："
    echo "   docker-compose up -d mysql8"
    exit 1
fi

echo "✅ MySQL 容器运行正常"

# 等待 MySQL 完全启动
echo "⏳ 等待 MySQL 服务完全启动..."
sleep 5

# 创建 demo 数据库和表结构
echo "🏗️  创建支持第三方认证的数据库表结构..."

docker exec -i ${CONTAINER_NAME} mysql -uroot -p${MYSQL_ROOT_PASSWORD} <<'EOF'
-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 创建数据库（指定字符集）
CREATE DATABASE IF NOT EXISTS demo 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE demo;

-- 删除已存在的表（避免冲突）
DROP TABLE IF EXISTS sys_user_third_auth;
DROP TABLE IF EXISTS sys_login_log;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_role_menu;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_menu;
DROP TABLE IF EXISTS sys_dept;

-- 创建部门表
CREATE TABLE sys_dept (
    id BIGINT NOT NULL COMMENT '部门ID（雪花算法生成）',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    leader VARCHAR(50) DEFAULT NULL COMMENT '负责人',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    status TINYINT DEFAULT 0 COMMENT '部门状态（0正常 1停用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标识（0正常 1删除）',
    version INT DEFAULT 1 COMMENT '版本号（乐观锁）',
    PRIMARY KEY (id),
    KEY idx_parent_id (parent_id),
    KEY idx_order_num (order_num),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 创建用户表（增强版 - 支持第三方认证）
CREATE TABLE sys_user (
    id BIGINT NOT NULL COMMENT '用户ID（雪花算法生成）',
    dept_id BIGINT DEFAULT NULL COMMENT '部门ID',
    employee_id VARCHAR(32) DEFAULT NULL COMMENT '工号',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
    real_name VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    password VARCHAR(100) DEFAULT NULL COMMENT '密码（本地认证使用）',
    salt VARCHAR(20) DEFAULT NULL COMMENT '盐值',
    email VARCHAR(100) DEFAULT NULL COMMENT '用户邮箱',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
    sex TINYINT DEFAULT 0 COMMENT '用户性别（0男 1女 2未知）',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像路径',
    status TINYINT DEFAULT 0 COMMENT '帐号状态（0正常 1停用 2锁定）',
    login_fail_count INT DEFAULT 0 COMMENT '登录失败次数',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(128) DEFAULT NULL COMMENT '最后登录IP',
    pwd_update_time DATETIME DEFAULT NULL COMMENT '密码最后更新时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标识（0正常 1删除）',
    version INT DEFAULT 1 COMMENT '版本号（乐观锁）',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_employee_id (employee_id),
    KEY idx_dept_id (dept_id),
    KEY idx_email (email),
    KEY idx_phone (phone),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表（支持第三方认证）';

-- 创建第三方认证表
CREATE TABLE sys_user_third_auth (
    id BIGINT NOT NULL COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    auth_type VARCHAR(20) NOT NULL COMMENT '认证类型（wechat-微信 work_wechat-企业微信 feishu-飞书 dingtalk-钉钉）',
    open_id VARCHAR(128) NOT NULL COMMENT '第三方平台用户唯一标识',
    union_id VARCHAR(128) DEFAULT NULL COMMENT '第三方平台用户统一标识',
    nick_name VARCHAR(100) DEFAULT NULL COMMENT '第三方平台昵称',
    avatar_url VARCHAR(500) DEFAULT NULL COMMENT '第三方平台头像URL',
    access_token VARCHAR(512) DEFAULT NULL COMMENT '访问令牌',
    refresh_token VARCHAR(512) DEFAULT NULL COMMENT '刷新令牌',
    expires_in INT DEFAULT NULL COMMENT '令牌过期时间（秒）',
    scope VARCHAR(255) DEFAULT NULL COMMENT '授权范围',
    bind_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    status TINYINT DEFAULT 0 COMMENT '绑定状态（0正常 1解绑）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标识（0正常 1删除）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_auth_type_open_id (auth_type, open_id),
    KEY idx_user_id (user_id),
    KEY idx_auth_type (auth_type),
    KEY idx_open_id (open_id),
    KEY idx_union_id (union_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户第三方认证绑定表';

-- 创建登录日志表
CREATE TABLE sys_login_log (
    id BIGINT NOT NULL COMMENT '日志ID',
    user_id BIGINT DEFAULT NULL COMMENT '用户ID',
    username VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    employee_id VARCHAR(32) DEFAULT NULL COMMENT '工号',
    login_type VARCHAR(20) DEFAULT NULL COMMENT '登录类型（password-密码 wechat-微信 work_wechat-企业微信 feishu-飞书）',
    status TINYINT DEFAULT 0 COMMENT '登录状态（0成功 1失败）',
    ip_addr VARCHAR(128) DEFAULT NULL COMMENT '登录IP地址',
    login_location VARCHAR(255) DEFAULT NULL COMMENT '登录地点',
    browser VARCHAR(50) DEFAULT NULL COMMENT '浏览器类型',
    os VARCHAR(50) DEFAULT NULL COMMENT '操作系统',
    msg VARCHAR(255) DEFAULT NULL COMMENT '提示消息',
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_username (username),
    KEY idx_employee_id (employee_id),
    KEY idx_login_type (login_type),
    KEY idx_status (status),
    KEY idx_ip_addr (ip_addr),
    KEY idx_login_time (login_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统登录日志表';

-- 创建角色表
CREATE TABLE sys_role (
    id BIGINT NOT NULL COMMENT '角色ID（雪花算法生成）',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_key VARCHAR(50) NOT NULL COMMENT '角色权限字符串',
    role_sort INT DEFAULT 0 COMMENT '显示顺序',
    data_scope TINYINT DEFAULT 1 COMMENT '数据范围（1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5仅本人数据权限）',
    menu_check_strictly TINYINT DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
    dept_check_strictly TINYINT DEFAULT 1 COMMENT '部门树选择项是否关联显示',
    status TINYINT DEFAULT 0 COMMENT '角色状态（0正常 1停用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标识（0正常 1删除）',
    version INT DEFAULT 1 COMMENT '版本号（乐观锁）',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_key (role_key),
    KEY idx_role_sort (role_sort),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 创建菜单权限表
CREATE TABLE sys_menu (
    id BIGINT NOT NULL COMMENT '菜单ID（雪花算法生成）',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    path VARCHAR(200) DEFAULT '' COMMENT '路由地址',
    component VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    query VARCHAR(255) DEFAULT NULL COMMENT '路由参数',
    is_frame TINYINT DEFAULT 1 COMMENT '是否为外链（0是 1否）',
    is_cache TINYINT DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
    menu_type CHAR(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    visible CHAR(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
    status CHAR(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    perms VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    icon VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标识（0正常 1删除）',
    version INT DEFAULT 1 COMMENT '版本号（乐观锁）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_parent_id (parent_id),
    KEY idx_order_num (order_num),
    KEY idx_menu_type (menu_type),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- 创建用户和角色关联表
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id),
    KEY idx_user_id (user_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户和角色关联表';

-- 创建角色和菜单关联表
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id),
    KEY idx_role_id (role_id),
    KEY idx_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和菜单关联表';

-- 插入初始数据
-- 部门数据
INSERT INTO sys_dept (id, parent_id, dept_name, order_num, leader, phone, email, status) VALUES
(100, 0, '总部', 0, '管理员', '13800138000', 'admin@demo.com', 0),
(101, 100, '研发部门', 1, '张三', '13800138001', 'zhangsan@demo.com', 0),
(102, 100, '市场部门', 2, '李四', '13800138002', 'lisi@demo.com', 0),
(103, 100, '测试部门', 3, '王五', '13800138003', 'wangwu@demo.com', 0)
ON DUPLICATE KEY UPDATE dept_name=VALUES(dept_name);

-- 用户数据（包含工号和增强字段）
INSERT INTO sys_user (id, dept_id, employee_id, username, nickname, real_name, password, salt, email, phone, sex, status) VALUES
(1, 101, 'EMP001', 'admin', '管理员', '系统管理员', '$2a$10$N.zmdr9k7uOCQb0bta/OauRxaOKSr.QhqyD2R5FKvMQjmHoLkm5Sy', '123456', 'admin@demo.com', '13800138000', 0, 0),
(2, 101, 'EMP002', 'test', '测试用户', '测试人员', '$2a$10$N.zmdr9k7uOCQb0bta/OauRxaOKSr.QhqyD2R5FKvMQjmHoLkm5Sy', '123456', 'test@demo.com', '13800138001', 1, 0),
(3, 103, 'EMP003', 'developer', '开发者', '开发工程师', '$2a$10$N.zmdr9k7uOCQb0bta/OauRxaOKSr.QhqyD2R5FKvMQjmHoLkm5Sy', '123456', 'dev@demo.com', '13800138002', 0, 0)
ON DUPLICATE KEY UPDATE username=VALUES(username);

-- 第三方认证示例数据
INSERT INTO sys_user_third_auth (id, user_id, auth_type, open_id, union_id, nick_name, status) VALUES
(1, 1, 'wechat', 'wechat_openid_admin', 'wechat_unionid_admin', '管理员微信', 0),
(2, 2, 'feishu', 'feishu_openid_test', 'feishu_unionid_test', '测试用户飞书', 0)
ON DUPLICATE KEY UPDATE open_id=VALUES(open_id);

-- 角色数据
INSERT INTO sys_role (id, role_name, role_key, role_sort, data_scope, status, remark) VALUES
(1, '超级管理员', 'admin', 1, 1, 0, '超级管理员'),
(2, '普通用户', 'user', 2, 2, 0, '普通用户'),
(3, '测试人员', 'tester', 3, 3, 0, '测试人员')
ON DUPLICATE KEY UPDATE role_name=VALUES(role_name);

-- 菜单数据
INSERT INTO sys_menu (id, menu_name, parent_id, order_num, path, component, menu_type, status, perms, icon) VALUES
(1, '系统管理', 0, 1, 'system', '', 'M', '0', '', 'system'),
(2, '用户管理', 1, 1, 'user', 'system/user/index', 'C', '0', 'system:user:list', 'user'),
(3, '角色管理', 1, 2, 'role', 'system/role/index', 'C', '0', 'system:role:list', 'peoples'),
(4, '菜单管理', 1, 3, 'menu', 'system/menu/index', 'C', '0', 'system:menu:list', 'tree-table'),
(5, '部门管理', 1, 4, 'dept', 'system/dept/index', 'C', '0', 'system:dept:list', 'tree'),
(100, '用户查询', 2, 1, '', '', 'F', '0', 'system:user:query', '#'),
(101, '用户新增', 2, 2, '', '', 'F', '0', 'system:user:add', '#'),
(102, '用户修改', 2, 3, '', '', 'F', '0', 'system:user:edit', '#'),
(103, '用户删除', 2, 4, '', '', 'F', '0', 'system:user:remove', '#'),
(104, '重置密码', 2, 5, '', '', 'F', '0', 'system:user:resetPwd', '#')
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name);

-- 用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 3)
ON DUPLICATE KEY UPDATE user_id=VALUES(user_id);

-- 角色菜单关联
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 100), (1, 101), (1, 102), (1, 103), (1, 104),
(2, 2), (2, 100),
(3, 2), (3, 3), (3, 100), (3, 101)
ON DUPLICATE KEY UPDATE role_id=VALUES(role_id);

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 验证数据
SELECT '✅ demo 数据库初始化完成!' as message;
SELECT '📊 初始化数据统计:' as info;
SELECT 
    (SELECT COUNT(*) FROM sys_dept) as dept_count,
    (SELECT COUNT(*) FROM sys_user) as user_count,
    (SELECT COUNT(*) FROM sys_user_third_auth) as third_auth_count,
    (SELECT COUNT(*) FROM sys_role) as role_count,
    (SELECT COUNT(*) FROM sys_menu) as menu_count;
EOF

# 检查执行结果
if [ $? -eq 0 ]; then
    echo ""
    echo "🎉 demo 数据库初始化成功！"
    echo "========================="
    echo "📋 数据库信息："
    echo "   - 数据库名: $DATABASE_NAME"
    echo "   - 容器名: $CONTAINER_NAME"
    echo "   - 连接地址: $MYSQL_HOST:$MYSQL_PORT"
    echo "   - 用户名: root"
    echo "   - 密码: $MYSQL_ROOT_PASSWORD"
    echo ""
    echo "🔐 默认账号："
    echo "   - 管理员: admin / admin123 (工号: EMP001)"
    echo "   - 测试用户: test / admin123 (工号: EMP002)"
    echo "   - 开发者: developer / admin123 (工号: EMP003)"
    echo ""
    echo "📱 第三方认证支持："
    echo "   - 微信认证 (wechat_openid_admin)"
    echo "   - 飞书认证 (feishu_openid_test)"
    echo ""
    echo "📊 初始化数据："
    echo "   - 部门表: 4条记录"
    echo "   - 用户表: 3条记录（含工号）"
    echo "   - 第三方认证: 2条记录"
    echo "   - 角色表: 3条记录"
    echo "   - 菜单表: 10条记录"
    echo ""
    echo "💡 连接测试命令："
    echo "   docker exec -it $CONTAINER_NAME mysql -uroot -p$MYSQL_ROOT_PASSWORD $DATABASE_NAME"
    echo ""
    echo "🔧 应用配置（application.yml）："
    echo "spring:"
    echo "  datasource:"
    echo "    url: jdbc:mysql://$MYSQL_HOST:$MYSQL_PORT/$DATABASE_NAME?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true"
    echo "    username: root"
    echo "    password: $MYSQL_ROOT_PASSWORD"
else
    echo "❌ 数据库初始化失败"
    echo "💡 请检查："
    echo "   1. MySQL 容器是否正常运行"
    echo "   2. 容器名是否为 $CONTAINER_NAME"
    echo "   3. root 密码是否正确"
    exit 1
fi