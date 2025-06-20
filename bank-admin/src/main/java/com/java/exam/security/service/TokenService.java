package com.java.exam.security.service;

import com.java.exam.security.LoginUser;
import com.java.exam.utils.RedisUtil;
import com.java.exam.utils.StringUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 */
@Component
public class TokenService
{
    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expireTime}")
    private Long expireTime;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 获取用户身份信息
     */
    public LoginUser getUserInfo(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token))
        {
            try {
                String key = Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                        .parseClaimsJws(token).getBody()
                        .get("login-key").toString();

                return (LoginUser) redisUtil.get(key);
            } catch (Exception e) {
            }
        }
        return null;
    }


    /**
     * 创建令牌
     */
    public String createToken(LoginUser user)
    {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + (expireTime*60*1000);
        Date exp = new Date(expMillis);

        redisUtil.set(user.getUsername(),user,expireTime, TimeUnit.MINUTES);

        Map<String, Object> map = new HashMap<>();
        map.put("login-key",user.getUsername());

        // 设置jwt的body
        return Jwts.builder()
                .setClaims(map)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secret.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp).compact();
    }



}
