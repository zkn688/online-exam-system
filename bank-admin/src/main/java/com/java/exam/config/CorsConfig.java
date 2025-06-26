package com.java.exam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址 - 使用addAllowedOrigin而不是addAllowedOriginPattern
        // 由于Spring Boot 2.2.7不支持addAllowedOriginPattern
        // 添加常用的前端开发地址
        config.addAllowedOrigin("http://localhost:8080"); // Vue默认端口
        config.addAllowedOrigin("http://localhost:8081");
        config.addAllowedOrigin("http://localhost:3000"); // React默认端口
        config.addAllowedOrigin("http://127.0.0.1:8080");
        config.addAllowedOrigin("http://127.0.0.1:8081");
        config.addAllowedOrigin("http://127.0.0.1:3000");

        // 添加前端开发机器的IP地址，添加多个常用端口
        config.addAllowedOrigin("http://192.168.11.58:8080");
        config.addAllowedOrigin("http://192.168.11.58:8081");
        config.addAllowedOrigin("http://192.168.11.58:3000");
        config.addAllowedOrigin("http://192.168.11.58:8000");
        config.addAllowedOrigin("http://192.168.11.58:8015");
        config.addAllowedOrigin("http://192.168.11.58:9000");
        config.addAllowedOrigin("http://192.168.11.58");

        // 允许所有跨域请求 - 仅在开发环境使用
        config.addAllowedOrigin("*"); // 临时允许所有源，便于调试

        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 暴露header中的其他属性给客户端应用程序
        config.addExposedHeader("authorization");
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
