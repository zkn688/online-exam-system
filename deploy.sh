#!/bin/bash

# 记录开始时间
echo "===== 开始部署 $(date) ====="

# 确保Docker和Docker Compose已安装
if ! command -v docker &> /dev/null; then
    echo "错误: Docker未安装，请先安装Docker"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "错误: Docker Compose未安装，请先安装Docker Compose"
    exit 1
fi

# 停止并删除旧容器
echo "停止并删除旧容器..."
docker-compose down

# 可选：清理未使用的Docker资源
echo "清理未使用的Docker资源..."
docker system prune -f

# 拉取最新代码
echo "拉取最新代码..."
git pull

# 构建并启动容器
echo "构建并启动容器..."
docker-compose up -d --build

# 等待容器启动完成
echo "等待容器启动完成..."
sleep 10

# 显示容器状态
echo "容器状态："
docker-compose ps

# 检查主应用是否正常启动
if [ "$(docker-compose ps -q app)" ] && [ "$(docker inspect -f {{.State.Running}} $(docker-compose ps -q app))" = "true" ]; then
    echo "后端应用启动成功！"
else
    echo "警告：后端应用可能未正常启动，请检查日志"
    docker-compose logs app
fi

echo "===== 部署完成！$(date) ====="
