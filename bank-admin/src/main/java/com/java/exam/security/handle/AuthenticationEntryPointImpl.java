package com.java.exam.security.handle;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.exam.exception.ErrorCode;
import com.java.exam.utils.HttpResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable
{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        ObjectMapper objectMapper = new ObjectMapper();
        HttpResult error = HttpResult.error(ErrorCode.UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
