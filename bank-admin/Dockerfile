FROM maven:3.6-jdk-8-slim AS build
WORKDIR /app
COPY pom.xml .
COPY checkstyle.xml .
# 下载依赖，利用Docker缓存机制
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:8-jre-slim
WORKDIR /app

# 安装字体库和相关依赖
RUN apt-get update && apt-get install -y --no-install-recommends \
    fontconfig \
    libfreetype6 \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

# 添加等待脚本，用于等待MySQL和Redis就绪
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.9.0/wait /wait
RUN chmod +x /wait

# 暴露应用端口
EXPOSE 9001

# 启动命令
CMD /wait && java -jar app.jar
