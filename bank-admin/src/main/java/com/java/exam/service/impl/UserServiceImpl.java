package com.java.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.exam.dto.AddUserDto;
import com.java.exam.dto.LoginDto;
import com.java.exam.dto.RegisterDto;
import com.java.exam.dto.UpdateUserInfoDto;
import com.java.exam.entity.User;
import com.java.exam.exception.BaseException;
import com.java.exam.exception.ErrorCode;
import com.java.exam.loggin.Log;
import com.java.exam.mapper.UserMapper;
import com.java.exam.security.LoginUser;
import com.java.exam.security.service.TokenService;
import com.java.exam.service.UserService;
import com.java.exam.utils.PageResponse;
import com.java.exam.utils.RedisUtil;
import com.java.exam.utils.SecurityUtils;
import com.java.exam.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

import static com.java.exam.utils.QueryWrapperUtils.setLikeWrapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final TokenService tokenService;

    private final RedisUtil redisUtil;

    private final AuthenticationManager authenticationManager;

    @Override
    public Boolean checkUsername(String username) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("username", username)) < 1;
    }

    @Override
    public String login(LoginDto loginDto) {

        // 用户验证
        Authentication authentication = null;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        } catch (Exception e) {

            if (e instanceof BadCredentialsException){
                throw new BaseException(ErrorCode.E_100101);
            }else {
                throw new BaseException(ErrorCode.E_100101.getCode(),e.getMessage());
            }
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public User updateUserInfo(UpdateUserInfoDto updateUser) {
        User user = getUserByUsername(updateUser.getUsername());
        user.setEmail(updateUser.getEmail());
        if (StringUtils.isNotEmpty(updateUser.getPassword())){
            user.setPassword(SecurityUtils.encryptPassword(updateUser.getPassword()));
            redisUtil.del(user.getUsername());
        }
        user.setTrueName(updateUser.getTrueName());
        user.setEmail(updateUser.getEmail());

        if (updateUser.getRoleId() != null && !Objects.equals(updateUser.getRoleId(), user.getRoleId())
                && SecurityUtils.getRoleId() == 3){
            user.setRoleId(updateUser.getRoleId());
            redisUtil.del(user.getUsername());
        }
        userMapper.updateById(user);
        return user;
    }

    @Override
    public PageResponse<User> getUser(String loginName, String trueName, Integer pageNo, Integer pageSize) {
        IPage<User> userPage = new Page<>(pageNo, pageSize);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("username", loginName);
        queryParams.put("true_name", trueName);

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        setLikeWrapper(wrapper, queryParams);
        wrapper.orderByDesc("role_id", "create_date");
        wrapper.orderByAsc("status");
        wrapper.ne("id",1);

        userPage = userMapper.selectPage(userPage, wrapper);
        List<User> records = userPage.getRecords();

        return PageResponse.<User>builder().data(records).total(userPage.getTotal()).build();
    }

    @Log("更改用户状态")
    @Override
    public void handlerUser(Integer type, String userIds) {
        // 转换成数组 需要操作的用户的id数组
        String[] ids = userIds.split(",");

        for (String id : ids) {

            User user = userMapper.selectById(Integer.parseInt(id));

            if (type == 1){
                user.setStatus(1);
                userMapper.updateById(user);
            }else if (type == 2){
                user.setStatus(2);
                userMapper.updateById(user);
            }else if (type == 3){
                userMapper.deleteById(Integer.parseInt(id));
            }else {
                throw new BaseException(ErrorCode.E_100105);
            }

            redisUtil.del(user.getUsername());

        }
    }

    @Override
    public void addUser(AddUserDto addUserDto) {
        String newPwd = SecurityUtils.encryptPassword(addUserDto.getPassword());
        User user = new User();
        BeanUtils.copyProperties(addUserDto, user);
        user.setPassword(newPwd);
        user.setEmail(addUserDto.getEmail());
        user.setCreateDate(new Date());
        userMapper.insert(user);
    }

    @Override
    public List<User> getUserInfoByIds(List<Integer> userIds) {
        if (ObjectUtils.isEmpty(userIds)){
            return new ArrayList<>();
        }
        return userMapper.selectBatchIds(userIds);
    }

    @Override
    public void register(RegisterDto registerDto) {
        if (!checkUsername(registerDto.getUsername())) {
            throw new BaseException(ErrorCode.E_100103);
        }

        User user = new User();
        BeanUtils.copyProperties(registerDto, user);
        user.setPassword(SecurityUtils.encryptPassword(registerDto.getPassword()));
        user.setRoleId(1);
        user.setCreateDate(new Date());

        userMapper.insert(user);
    }
}
