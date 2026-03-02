@echo off
chcp 65001 >nul
echo 开始创建 demo 数据库...

set DB_HOST=127.0.0.1
set DB_PORT=3307
set DB_USER=root
set DB_PASS=root
set DB_NAME=demo

echo 正在执行数据库创建脚本...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% < common\src\main\resources\sql\create-demo-database.sql

if %errorlevel% equ 0 (
    echo ✅ demo 数据库创建成功！
    echo 📊 数据库信息：
    echo    - 主机: %DB_HOST%:%DB_PORT%
    echo    - 用户名: %DB_USER%
    echo    - 数据库: %DB_NAME%
    echo.
    echo 📋 初始化数据：
    echo    - 用户表^(sys_user^): 2条记录
    echo    - 角色表^(sys_role^): 2条记录
    echo    - 菜单表^(sys_menu^): 4条记录
    echo.
    echo 🔐 默认账号：
    echo    - 管理员: admin / admin123
    echo    - 测试用户: test / admin123
) else (
    echo ❌ 数据库创建失败，请检查 MySQL 服务是否正常运行
    pause
    exit /b 1
)

pause