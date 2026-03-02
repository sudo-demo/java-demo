#!/bin/bash

# 通用数据库升级框架脚本
# 支持增量更新，保护现有数据
echo "🔄 通用数据库升级框架"
echo "========================"

# 配置信息
CONTAINER_NAME=${1:-"mysql8"}
MYSQL_ROOT_PASSWORD=${2:-"root"}
MYSQL_PORT=${3:-"3307"}
MYSQL_HOST=${4:-"127.0.0.1"}
DATABASE_NAME=${5:-"demo"}

echo "📋 升级配置："
echo "   容器名: $CONTAINER_NAME"
echo "   数据库: $DATABASE_NAME"
echo "   连接: $MYSQL_HOST:$MYSQL_PORT"
echo ""

# 检查容器状态
check_container() {
    echo "🔍 检查 MySQL 容器状态..."
    if ! docker ps | grep -q ${CONTAINER_NAME}; then
        echo "❌ 容器 ${CONTAINER_NAME} 未运行"
        echo "💡 请先启动您的 MySQL 容器："
        echo "   docker-compose up -d ${CONTAINER_NAME}"
        return 1
    fi
    echo "✅ MySQL 容器运行正常"
    return 0
}

# 执行SQL升级脚本
execute_upgrade_sql() {
    local sql_script="$1"
    echo "⚙️  执行升级脚本: $sql_script"
    
    docker exec -i ${CONTAINER_NAME} mysql -uroot -p${MYSQL_ROOT_PASSWORD} ${DATABASE_NAME} <<EOF
-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 执行传入的SQL脚本
$(cat "$sql_script")

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;
EOF
}

# 创建字段检查函数
create_field_check_function() {
    cat <<'EOF'
DELIMITER $$

-- 检查字段是否存在并添加的通用函数
CREATE PROCEDURE check_and_add_column(
    IN table_name VARCHAR(64),
    IN column_name VARCHAR(64),
    IN column_definition TEXT,
    IN index_definition TEXT
)
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END;
    
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE()
        AND TABLE_NAME = table_name 
        AND COLUMN_NAME = column_name
    ) THEN
        SET @sql = CONCAT('ALTER TABLE ', table_name, ' ADD COLUMN ', column_definition);
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        
        -- 如果需要创建索引
        IF index_definition IS NOT NULL AND index_definition != '' THEN
            SET @sql = CONCAT('ALTER TABLE ', table_name, ' ADD ', index_definition);
            PREPARE stmt FROM @sql;
            EXECUTE stmt;
            DEALLOCATE PREPARE stmt;
        END IF;
        
        SELECT CONCAT('✅ 已添加字段: ', table_name, '.', column_name) as message;
    ELSE
        SELECT CONCAT('⚠️  字段已存在: ', table_name, '.', column_name) as message;
    END IF;
END$$

DELIMITER ;
EOF
}

# 主升级流程
main_upgrade() {
    echo "🚀 开始数据库升级..."
    
    # 创建临时SQL文件
    local temp_sql="/tmp/db_upgrade_temp.sql"
    
    # 生成升级SQL
    cat > "$temp_sql" <<EOF
$(create_field_check_function)

-- 使用数据库
USE ${DATABASE_NAME};

-- 执行具体升级逻辑
-- 这里可以添加具体的表结构变更
CALL check_and_add_column('sys_user', 'employee_id', "employee_id VARCHAR(32) DEFAULT NULL COMMENT '工号'", "UNIQUE INDEX uk_employee_id (employee_id)");
CALL check_and_add_column('sys_user', 'login_fail_count', "login_fail_count INT DEFAULT 0 COMMENT '登录失败次数'", NULL);
CALL check_and_add_column('sys_user', 'last_login_time', "last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间'", NULL);

-- 清理临时函数
DROP PROCEDURE IF EXISTS check_and_add_column;
EOF
    
    # 执行升级
    docker exec -i ${CONTAINER_NAME} mysql -uroot -p${MYSQL_ROOT_PASSWORD} < "$temp_sql"
    
    # 清理临时文件
    rm -f "$temp_sql"
    
    if [ $? -eq 0 ]; then
        echo "✅ 数据库升级成功完成！"
        return 0
    else
        echo "❌ 数据库升级失败"
        return 1
    fi
}

# 显示帮助信息
show_help() {
    echo "使用方法:"
    echo "  $0 [容器名] [密码] [端口] [主机] [数据库名]"
    echo ""
    echo "参数说明:"
    echo "  容器名     - MySQL容器名称 (默认: mysql8)"
    echo "  密码       - root用户密码 (默认: root)"
    echo "  端口       - MySQL端口 (默认: 3307)"
    echo "  主机       - MySQL主机地址 (默认: 127.0.0.1)"
    echo "  数据库名   - 目标数据库名称 (默认: demo)"
    echo ""
    echo "示例:"
    echo "  $0 mysql8 root 3307 127.0.0.1 demo"
    echo "  $0  # 使用所有默认值"
}

# 参数处理
case "${1:-}" in
    -h|--help)
        show_help
        exit 0
        ;;
    "")
        # 使用默认参数
        ;;
    *)
        # 参数已通过位置参数传递
        ;;
esac

# 执行升级流程
if check_container; then
    main_upgrade
    exit $?
else
    exit 1
fi