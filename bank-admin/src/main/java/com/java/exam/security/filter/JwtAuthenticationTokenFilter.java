package com.java.exam.security.filter;


import com.java.exam.security.LoginUser;
import com.java.exam.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * token过滤器 验证token有效性
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    @Autowired
    private TokenService tokenService;

    private final Map<Integer, String> roleMap = new ConcurrentHashMap<>(3);

    public JwtAuthenticationTokenFilter() {
        roleMap.put(1, "student");
        roleMap.put(2, "teacher");
        roleMap.put(3, "admin");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        LoginUser userInfo = tokenService.getUserInfo(request);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (userInfo == null) {
            // 结束
            chain.doFilter(request, response);
            return;
        }
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(roleMap.get(userInfo.getRoleId())));
        // 配置token认证用户的权限
        UsernamePasswordAuthenticationToken userAuthorization = new UsernamePasswordAuthenticationToken(userInfo, null, roles);
        SecurityContextHolder.getContext().setAuthentication(userAuthorization);
        // 结束
        chain.doFilter(request, response);
    }
}
