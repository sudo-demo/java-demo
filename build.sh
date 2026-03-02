#!/bin/bash

echo "开始构建Demo微服务项目..."

# 清理并编译项目
mvn clean compile

if [ $? -eq 0 ]; then
    echo "项目编译成功！"
    echo "可以运行以下命令启动网关服务："
    echo "cd gateway && mvn spring-boot:run"
else
    echo "项目编译失败，请检查错误信息"
fi