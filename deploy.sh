#!/bin/bash

# 停止并删除旧容器
echo "停止并删除旧容器..."
docker-compose down

# 拉取最新代码
echo "拉取最新代码..."
git pull

# 构建并启动容器
echo "构建并启动容器..."
docker-compose up -d --build

# 显示容器状态
echo "容器状态："
docker-compose ps

echo "部署完成！"
