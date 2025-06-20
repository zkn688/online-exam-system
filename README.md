# 在线考试系统

这是一个基于Spring Boot的在线考试系统后端项目。

## 项目结构

- `bank-admin`：后端管理系统

## 技术栈

- Spring Boot
- MyBatis Plus
- Spring Security
- Redis
- MySQL

## 功能特点

- 用户管理：包括学生、教师和管理员角色
- 试题管理：试题录入、编辑、导入导出
- 试卷管理：手动组卷、智能组卷
- 考试管理：线上考试、自动评分
- 成绩管理：成绩统计、成绩查询

## 快速开始

### 环境要求

- JDK 8+
- Maven 3.6+
- MySQL 5.7+
- Redis

### 数据库配置

1. 创建数据库 `bank`
2. 运行 `bank-admin/bank.sql` 初始化数据库

### 配置文件

修改 `bank-admin/src/main/resources/application-dev.yml` 中的数据库和Redis配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bank?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: 你的数据库用户名
    password: 你的数据库密码
  
  redis:
    host: localhost
    port: 6379
    password: 你的Redis密码（如果有）
```

### 运行项目

```bash
cd bank-admin
mvn spring-boot:run
```

## API文档

启动项目后，访问 `http://localhost:8080/swagger-ui.html` 查看API文档。

## 联系方式

GitHub: [zkn688](https://github.com/zkn688) 