#!/bin/bash

# 使用图形化工具创建 demo 数据库的脚本
echo "🔄 准备创建 demo 数据库..."

# 检查系统类型
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    echo "💻 检测到 macOS 系统"
    echo "🔧 请使用以下方式之一创建数据库："
    echo ""
    echo "方式1 - 使用 Sequel Pro/Navicat 等图形化工具："
    echo "   连接信息："
    echo "   - 主机：127.0.0.1"
    echo "   - 端口：3307" 
    echo "   - 用户名：root"
    echo "   - 密码：root"
    echo "   然后执行 common/src/main/resources/sql/create-demo-database.sql 文件"
    echo ""
    echo "方式2 - 安装 MySQL 客户端："
    echo "   brew install mysql-client"
    echo "   然后重新运行此脚本"
    echo ""
    echo "方式3 - 使用 Docker（推荐）："
    echo "   docker-compose up -d"
    echo ""
    
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux
    echo "🐧 检测到 Linux 系统"
    echo "🔧 请使用以下方式之一创建数据库："
    echo ""
    echo "方式1 - 安装 MySQL 客户端："
    echo "   Ubuntu/Debian: sudo apt-get install mysql-client"
    echo "   CentOS/RHEL: sudo yum install mysql"
    echo "   然后重新运行此脚本"
    echo ""
    echo "方式2 - 使用图形化工具连接并执行SQL文件"
    echo ""
    echo "方式3 - 使用 Docker："
    echo "   docker-compose up -d"
    echo ""
    
else
    # Windows 或其他系统
    echo "🖥️  检测到 Windows 系统"
    echo "🔧 请使用以下方式之一创建数据库："
    echo ""
    echo "方式1 - 使用 Navicat/HeidiSQL 等图形化工具："
    echo "   连接信息："
    echo "   - 主机：127.0.0.1"
    echo "   - 端口：3307"
    echo "   - 用户名：root" 
    echo "   - 密码：root"
    echo "   然后执行 common\\src\\main\\resources\\sql\\create-demo-database.sql 文件"
    echo ""
    echo "方式2 - 使用 Docker："
    echo "   docker-compose up -d"
    echo ""
fi

echo "📝 SQL 文件路径："
echo "   $(pwd)/common/src/main/resources/sql/create-demo-database.sql"
echo ""
echo "💡 提示：您可以复制上述连接信息到任何 MySQL 客户端工具中使用"