#!/bin/bash

# 数据库增量更新脚本 - 添加第三方认证和登录限制功能
# 适用于已有数据库的升级，不会删除现有数据
echo "🔄 执行数据库增量更新..."
echo "================================"

# 使用您提供的配置
CONTAINER_NAME="mysql8"
MYSQL_ROOT_PASSWORD="root"
MYSQL_PORT="3307"
MYSQL_HOST="127.0.0.1"
DATABASE_NAME="demo"

echo "📋 配置信息："
echo "   容器名: $CONTAINER_NAME"
echo "   数据库: $DATABASE_NAME"
echo "   连接: $MYSQL_HOST:$MYSQL_PORT"
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

# 执行增量更新
echo "⚙️  执行数据库结构增量更新..."

docker exec -i ${CONTAINER_NAME} mysql -uroot -p${MYSQL_ROOT_PASSWORD} <<'EOF'
-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 选择数据库
USE demo;

-- 检查并添加用户表字段（如果不存在）
DELIMITER $$

CREATE PROCEDURE upgrade_sys_user_table()
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END;
    
    -- 添加工号字段
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = 'demo' 
        AND TABLE_NAME = 'sys_user' 
        AND COLUMN_NAME = 'employee_id'
    ) THEN
        ALTER TABLE sys_user ADD COLUMN employee_id VARCHAR(32) DEFAULT NULL COMMENT '工号';
        ALTER TABLE sys_user ADD UNIQUE INDEX uk_employee_id (employee_id);
        SELECT '✅ 已添加工号字段' as message;
    ELSE
        SELECT '⚠️  工号字段已存在' as message;
    END IF;
    
    -- 添加登录失败次数字段
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = 'demo' 
        AND TABLE_NAME = 'sys_user' 
        AND COLUMN_NAME = 'login_fail_count'
    ) THEN
        ALTER TABLE sys_user ADD COLUMN login_fail_count INT DEFAULT 0 COMMENT '登录失败次数';
        SELECT '✅ 已添加登录失败次数字段' as message;
    ELSE
        SELECT '⚠️  登录失败次数字段已存在' as message;
    END IF;
    
    -- 添加最后登录时间字段
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = 'demo' 
        AND TABLE_NAME = 'sys_user' 
        AND COLUMN_NAME = 'last_login_time'
    ) THEN
        ALTER TABLE sys_user ADD COLUMN last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间';
        SELECT '✅ 已添加最后登录时间字段' as message;
    ELSE
        SELECT '⚠️  最后登录时间字段已存在' as message;
    END IF;
    
    -- 添加最后登录IP字段
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = 'demo' 
        AND TABLE_NAME = 'sys_user' 
        AND COLUMN_NAME = 'last_login_ip'
    ) THEN
        ALTER TABLE sys_user ADD COLUMN last_login_ip VARCHAR(128) DEFAULT NULL COMMENT '最后登录IP';
        SELECT '✅ 已添加最后登录IP字段' as message;
    ELSE
        SELECT '⚠️  最后登录IP字段已存在' as message;
    END IF;
    
    -- 添加密码更新时间字段
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = 'demo' 
        AND TABLE_NAME = 'sys_user' 
        AND COLUMN_NAME = 'pwd_update_time'
    ) THEN
        ALTER TABLE sys_user ADD COLUMN pwd_update_time DATETIME DEFAULT NULL COMMENT '密码最后更新时间';
        SELECT '✅ 已添加密码更新时间字段' as message;
    ELSE
        SELECT '⚠️  密码更新时间字段已存在' as message;
    END IF;
    
    -- 修改状态字段注释（支持锁定状态）
    ALTER TABLE sys_user MODIFY COLUMN status TINYINT DEFAULT 0 COMMENT '帐号状态（0正常 1停用 2锁定）';
    SELECT '✅ 已更新状态字段注释' as message;
END$$

DELIMITER ;

-- 执行用户表升级
CALL upgrade_sys_user_table();
DROP PROCEDURE IF EXISTS upgrade_sys_user_table;

-- 创建第三方认证表（如果不存在）
CREATE TABLE IF NOT EXISTS sys_user_third_auth (
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

SELECT '✅ 第三方认证表检查完成' as message;

-- 创建登录日志表（如果不存在）
CREATE TABLE IF NOT EXISTS sys_login_log (
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

SELECT '✅ 登录日志表检查完成' as message;

-- 为现有用户添加工号（如果没有的话）
UPDATE sys_user 
SET employee_id = CONCAT('EMP', LPAD(id, 3, '0')) 
WHERE employee_id IS NULL OR employee_id = '';

SELECT CONCAT('✅ 已为 ', ROW_COUNT(), ' 个用户分配工号') as message;

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 显示更新结果
SELECT '🎉 数据库增量更新完成!' as message;
SELECT '📊 当前表结构统计:' as info;
SELECT 
    (SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'demo') as table_count,
    (SELECT COUNT(*) FROM sys_user) as user_count,
    (SELECT COUNT(*) FROM sys_user_third_auth) as third_auth_count,
    (SELECT COUNT(*) FROM sys_login_log) as login_log_count;
EOF

# 检查执行结果
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 数据库增量更新成功！"
    echo "========================"
    echo "📋 更新内容："
    echo "   ✓ 用户表字段增强（工号、登录限制等）"
    echo "   ✓ 第三方认证表创建"
    echo "   ✓ 登录日志表创建"
    echo "   ✓ 现有数据兼容性处理"
    echo ""
    echo "💡 后续操作建议："
    echo "   1. 检查应用是否能正常连接数据库"
    echo "   2. 验证新增字段功能是否正常"
    echo "   3. 测试第三方认证集成"
    echo "   4. 配置登录安全策略"
else
    echo "❌ 数据库增量更新失败"
    echo "💡 请检查："
    echo "   1. MySQL 容器是否正常运行"
    echo "   2. 数据库连接信息是否正确"
    echo "   3. 是否有足够的权限执行DDL操作"
    exit 1
fi