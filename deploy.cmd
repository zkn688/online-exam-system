@echo off
echo ===== 开始部署 %date% %time% =====

REM 停止并删除旧容器
echo 停止并删除旧容器...
docker-compose down

REM 清理未使用的Docker资源
echo 清理未使用的Docker资源...
docker system prune -f

REM 拉取最新代码
echo 拉取最新代码...
git pull

REM 构建并启动容器
echo 构建并启动容器...
docker-compose up -d --build

REM 等待容器启动完成
echo 等待容器启动完成...
timeout /t 10

REM 显示容器状态
echo 容器状态：
docker-compose ps

echo ===== 部署完成！%date% %time% =====
