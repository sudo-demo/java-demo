#!/bin/bash

# Docker MySQL demo 数据库初始化脚本
echo "🐳 开始初始化 Docker MySQL demo 数据库..."

# Docker MySQL 连接配置
CONTAINER_NAME="mysql-demo"  # 根据您的实际容器名修改
MYSQL_ROOT_PASSWORD="root"   # 根据您的实际密码修改
MYSQL_PORT="3307"            # 根据您的实际端口修改

# 检查容器是否存在并运行
echo "🔍 检查 MySQL 容器状态..."
if ! docker ps | grep -q ${CONTAINER_NAME}; then
    echo "❌ MySQL 容器 ${CONTAINER_NAME} 未运行"
    echo "💡 请先启动您的 MySQL 容器"
    exit 1
fi

echo "✅ MySQL 容器运行正常"

# 创建 demo 数据库
echo "🏗️  创建 demo 数据库..."
docker exec -i ${CONTAINER_NAME} mysql -uroot -p${MYSQL_ROOT_PASSWORD} <<EOF
CREATE DATABASE IF NOT EXISTS demo 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE demo;

-- 创建用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT NOT NULL COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    status TINYINT DEFAULT 0 COMMENT '状态（0-正常，1-禁用）',
    dept_id BIGINT DEFAULT NULL COMMENT '部门ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标识（0-正常，1-删除）',
    version INT DEFAULT 1 COMMENT '版本号（乐观锁）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_email (email),
    KEY idx_phone (phone),
    KEY idx_dept_id (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 创建角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT NOT NULL COMMENT '主键ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_key VARCHAR(50) NOT NULL COMMENT '角色权限字符串',
    role_sort INT DEFAULT 0 COMMENT '显示顺序',
    status TINYINT DEFAULT 0 COMMENT '角色状态（0-正常，1-停用）',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标识（0-正常，1-删除）',
    version INT DEFAULT 1 COMMENT '版本号（乐观锁）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_key (role_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 插入初始数据
INSERT INTO sys_user (id, username, password, nickname, email, phone, status) VALUES
(1, 'admin', '\$2a\$10\$N.zmdr9k7uOCQb0bta/OauRxaOKSr.QhqyD2R5FKvMQjmHoLkm5Sy', '管理员', 'admin@demo.com', '13800138000', 0),
(2, 'test', '\$2a\$10\$N.zmdr9k7uOCQb0bta/OauRxaOKSr.QhqyD2R5FKvMQjmHoLkm5Sy', '测试用户', 'test@demo.com', '13800138001', 0)
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO sys_role (id, role_name, role_key, role_sort, status, remark) VALUES
(1, '超级管理员', 'admin', 1, 0, '超级管理员'),
(2, '普通用户', 'user', 2, 0, '普通用户')
ON DUPLICATE KEY UPDATE role_name=role_name;

-- 验证数据
SELECT '✅ 数据库初始化完成!' as message;
SELECT COUNT(*) as user_count FROM sys_user;
SELECT COUNT(*) as role_count FROM sys_role;
EOF

if [ $? -eq 0 ]; then
    echo "🎉 demo 数据库初始化成功！"
    echo "📊 数据库信息："
    echo "   - 数据库名: demo"
    echo "   - 容器名: ${CONTAINER_NAME}"
    echo "   - 端口: ${MYSQL_PORT}"
    echo ""
    echo "🔐 默认账号："
    echo "   - 管理员: admin / admin123"
    echo "   - 测试用户: test / admin123"
    echo ""
    echo "💡 连接信息："
    echo "   docker exec -it ${CONTAINER_NAME} mysql -uroot -p${MYSQL_ROOT_PASSWORD} demo"
else
    echo "❌ 数据库初始化失败"
    exit 1
fi