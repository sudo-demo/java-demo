#!/bin/bash

# 重启服务脚本
# 用于重启后端系统服务和网关服务，会先检查服务状态并停止运行中的服务
# 用法:
#   ./restart-services.sh          # 重启所有服务（后端 9998 + 网关 9999）
#   ./restart-services.sh <端口>   # 只重启指定端口的服务

echo "========================================"
echo "开始重启服务..."
echo "========================================"

# 定义默认端口
DEFAULT_BACKEND_PORT=9998
DEFAULT_GATEWAY_PORT=9999
DEFAULT_MANAGE_PORT=9996

# 解析命令行参数
SPECIFIC_PORT=$1

# 定义脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# 定义日志文件和目录
BACKEND_LOG="$SCRIPT_DIR/backend.log"
GATEWAY_LOG="$SCRIPT_DIR/gateway.log"
MANAGE_LOG="$SCRIPT_DIR/manage.log"
BACKEND_DIR="$SCRIPT_DIR/system"
GATEWAY_DIR="$SCRIPT_DIR/gateway"
MANAGE_DIR="$SCRIPT_DIR/manage"

# 输出目录信息
echo "脚本目录: $SCRIPT_DIR"
echo "后端服务目录: $BACKEND_DIR"
echo "网关服务目录: $GATEWAY_DIR"
echo "后端日志文件: $BACKEND_LOG"
echo "网关日志文件: $GATEWAY_LOG"

# 重启服务函数（通用）
restart_service() {
    local PORT=$1
    local SERVICE_NAME=$2
    local LOG_FILE=$3
    local SERVICE_DIR=$4
    local MAIN_CLASS=$5

    echo ""
echo "========================================"
echo "重启${SERVICE_NAME} (端口: $PORT)..."
echo "========================================"

    # 检查服务是否在运行
    echo "检查${SERVICE_NAME}状态..."
    if lsof -i :$PORT > /dev/null; then
        # 找到并停止运行在端口 $PORT 的进程
        PID=$(lsof -t -i :$PORT)
        echo "${SERVICE_NAME}正在运行，进程ID: $PID"
        echo "停止${SERVICE_NAME}..."
        kill -9 $PID
        if [ $? -eq 0 ]; then
            echo "✅ ${SERVICE_NAME}已成功停止"
        else
            echo "❌ 停止${SERVICE_NAME}失败"
            return 1
        fi
    else
        echo "${SERVICE_NAME}未运行，直接启动"
    fi

    # 等待服务完全停止
    echo "等待服务完全停止..."
    sleep 3

    # 清理之前的日志文件
    echo "清理之前的日志文件..."
    if [ -f "$LOG_FILE" ]; then
        rm -f "$LOG_FILE"
        echo "日志文件已清理"
    fi

    # 重启服务
    echo "重启${SERVICE_NAME}..."
    cd "$SERVICE_DIR"

    # 在后台启动服务
    echo "启动${SERVICE_NAME} (在后台运行)..."
    nohup mvn clean compile spring-boot:run -Dspring-boot.run.main-class=$MAIN_CLASS > "$LOG_FILE" 2>&1 &

    # 记录启动的进程ID
    SERVICE_PID=$!
    echo "${SERVICE_NAME}启动中，进程ID: $SERVICE_PID"
    echo "${SERVICE_NAME}日志输出到: $LOG_FILE"

    # 等待服务启动
    echo "等待${SERVICE_NAME}启动..."
    sleep 20

    # 检查服务是否启动成功
    echo "检查${SERVICE_NAME}启动状态..."
    if lsof -i :$PORT > /dev/null; then
        # 获取实际运行的进程ID
        ACTUAL_PID=$(lsof -t -i :$PORT)
        echo "✅ ${SERVICE_NAME}启动成功！"
        echo "✅ 服务进程ID: $ACTUAL_PID"
        echo "✅ 运行地址: http://localhost:$PORT"
        echo "✅ 日志文件: $LOG_FILE"
        return 0
    else
        echo "❌ ${SERVICE_NAME}启动失败"
        echo "❌ 请查看日志文件获取详细信息: $LOG_FILE"
        echo "❌ 日志预览:"
        tail -n 20 "$LOG_FILE"
        return 1
    fi
}

# 执行重启操作
if [ -n "$SPECIFIC_PORT" ]; then
    # 指定了端口，只重启该端口的服务
    echo "指定端口: $SPECIFIC_PORT"
    
    case "$SPECIFIC_PORT" in
        "$DEFAULT_BACKEND_PORT")
            restart_service "$DEFAULT_BACKEND_PORT" "后端服务" "$BACKEND_LOG" "$BACKEND_DIR" "com.cobazaar.system.SystemApplication"
            RESULT=$?
            ;;
        "$DEFAULT_GATEWAY_PORT")
            restart_service "$DEFAULT_GATEWAY_PORT" "网关服务" "$GATEWAY_LOG" "$GATEWAY_DIR" "com.cobazaar.gateway.GatewayApplication"
            RESULT=$?
            ;;
        "$DEFAULT_MANAGE_PORT")
            restart_service "$DEFAULT_MANAGE_PORT" "管理服务" "$MANAGE_LOG" "$MANAGE_DIR" "com.cobazaar.manage.ManageApplication"
            RESULT=$?
            ;;
        *)
            echo "❌ 未知端口: $SPECIFIC_PORT"
            echo "支持的端口: $DEFAULT_BACKEND_PORT (后端), $DEFAULT_GATEWAY_PORT (网关)"
            exit 1
            ;;
    esac
else
    # 未指定端口，重启所有服务
    echo "未指定端口，重启所有服务"
    
    restart_service "$DEFAULT_BACKEND_PORT" "后端服务" "$BACKEND_LOG" "$BACKEND_DIR" "com.cobazaar.system.SystemApplication"
    BACKEND_RESULT=$?
    
    restart_service "$DEFAULT_GATEWAY_PORT" "网关服务" "$GATEWAY_LOG" "$GATEWAY_DIR" "com.cobazaar.gateway.GatewayApplication"
    GATEWAY_RESULT=$?

    restart_service "$DEFAULT_MANAGE_PORT" "管理服务" "$MANAGE_LOG" "$MANAGE_DIR" "com.cobazaar.manage.ManageApplication"
    MANAGE_RESULT=$?
fi

# 总结结果
echo ""
echo "========================================"
echo "服务重启完成！"
echo "========================================"

if [ -n "$SPECIFIC_PORT" ]; then
    # 指定了端口，显示单个服务的结果
    if [ $RESULT -eq 0 ]; then
        case "$SPECIFIC_PORT" in
            "$DEFAULT_BACKEND_PORT")
                echo "✅ 后端服务: 重启成功"
                echo "   运行地址: http://localhost:$DEFAULT_BACKEND_PORT"
                echo "   API文档: http://localhost:$DEFAULT_BACKEND_PORT/doc.html"
                ;;
            "$DEFAULT_GATEWAY_PORT")
                echo "✅ 网关服务: 重启成功"
                echo "   运行地址: http://localhost:$DEFAULT_GATEWAY_PORT"
                ;;
        esac
    else
        case "$SPECIFIC_PORT" in
            "$DEFAULT_BACKEND_PORT")
                echo "❌ 后端服务: 重启失败"
                ;;
            "$DEFAULT_GATEWAY_PORT")
                echo "❌ 网关服务: 重启失败"
                ;;
        esac
    fi
else
    # 未指定端口，显示所有服务的结果
    if [ $BACKEND_RESULT -eq 0 ]; then
        echo "✅ 后端服务: 重启成功"
        echo "   运行地址: http://localhost:$DEFAULT_BACKEND_PORT"
        echo "   API文档: http://localhost:$DEFAULT_BACKEND_PORT/doc.html"
    else
        echo "❌ 后端服务: 重启失败"
    fi

    if [ $GATEWAY_RESULT -eq 0 ]; then
        echo "✅ 网关服务: 重启成功"
        echo "   运行地址: http://localhost:$DEFAULT_GATEWAY_PORT"
    else
        echo "❌ 网关服务: 重启失败"
    fi

    if [ $MANAGE_RESULT -eq 0 ]; then
        echo "✅ 管理服务: 重启成功"
        echo "   运行地址: http://localhost:$DEFAULT_MANAGE_PORT"
        echo "   API文档: http://localhost:$DEFAULT_MANAGE_PORT/manage/doc.html"
    else
        echo "❌ 管理服务: 重启失败"
    fi
fi

echo "========================================"

# 显示使用说明
echo "使用说明:"
echo "  ./restart-services.sh          # 重启所有服务（后端 $DEFAULT_BACKEND_PORT + 网关 $DEFAULT_GATEWAY_PORT）"
echo "  ./restart-services.sh <端口>   # 只重启指定端口的服务"
echo "========================================"
