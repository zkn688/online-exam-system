# PowerShell部署脚本

# 记录开始时间
Write-Host "===== 开始部署 $(Get-Date) ====="

# 检查Docker是否已安装
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Host "错误: Docker未安装，请先安装Docker" -ForegroundColor Red
    exit 1
}

# 停止并删除旧容器
Write-Host "停止并删除旧容器..." -ForegroundColor Cyan
docker-compose down

# 清理未使用的Docker资源
Write-Host "清理未使用的Docker资源..." -ForegroundColor Cyan
docker system prune -f

# 拉取最新代码
Write-Host "拉取最新代码..." -ForegroundColor Cyan
git pull

# 构建并启动容器
Write-Host "构建并启动容器..." -ForegroundColor Cyan
docker-compose up -d --build

# 等待容器启动完成
Write-Host "等待容器启动完成..." -ForegroundColor Cyan
Start-Sleep -Seconds 10

# 显示容器状态
Write-Host "容器状态：" -ForegroundColor Cyan
docker-compose ps

# 检查主应用是否正常启动
$appContainerId = docker-compose ps -q app
$isRunning = docker inspect -f "{{.State.Running}}" $appContainerId

if ($appContainerId -and $isRunning -eq "true") {
    Write-Host "后端应用启动成功！" -ForegroundColor Green
}
else {
    Write-Host "警告：后端应用可能未正常启动，请检查日志" -ForegroundColor Yellow
    docker-compose logs app
}

Write-Host "===== 部署完成！$(Get-Date) ====="
