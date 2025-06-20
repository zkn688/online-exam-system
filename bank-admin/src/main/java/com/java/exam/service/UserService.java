package com.java.exam.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.exam.dto.AddUserDto;
import com.java.exam.dto.LoginDto;
import com.java.exam.dto.RegisterDto;
import com.java.exam.dto.UpdateUserInfoDto;
import com.java.exam.entity.User;
import com.java.exam.utils.PageResponse;

import java.util.List;


public interface UserService extends IService<User> {

    Boolean checkUsername(String username);

    String login(LoginDto loginDto);

    User getUserByUsername(String username);

    User updateUserInfo(UpdateUserInfoDto updateUserInfoDto);

    PageResponse<User> getUser(String loginName, String trueName, Integer pageNo, Integer pageSize);

    void handlerUser(Integer type, String userIds);

    void addUser(AddUserDto addUserDto);


    List<User> getUserInfoByIds(List<Integer> userIds);

    void register(RegisterDto registerDto);
}
