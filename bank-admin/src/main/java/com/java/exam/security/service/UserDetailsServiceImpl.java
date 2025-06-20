package com.java.exam.security.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.exam.entity.User;
import com.java.exam.exception.BaseException;
import com.java.exam.exception.ErrorCode;
import com.java.exam.mapper.UserMapper;
import com.java.exam.security.LoginUser;
import com.java.exam.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username)
    {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (StringUtils.isNull(user))
        {
            throw new BaseException(ErrorCode.E_100102);
        }else if (user.getStatus() != 1)
        {
            throw new BaseException(ErrorCode.E_100107);
        }

        return new LoginUser(user.getId().longValue(),user,user.getRoleId());
    }

}
