package com.java.exam.utils;

import com.java.exam.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全服务工具类
 */
public class SecurityUtils
{

    /**
     * 用户ID
     **/
    public static Integer getUserId()
    {
        return Math.toIntExact(getLoginUser().getUserId());
    }


    /**
     * 获取用户账户
     **/
    public static String getUsername()
    {
        return getLoginUser().getUsername();
    }

    /**
     * 获取用户权限
     **/
    public static Integer getRoleId()
    {
        return getLoginUser().getRoleId();
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        System.out.println(getAuthentication());
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
