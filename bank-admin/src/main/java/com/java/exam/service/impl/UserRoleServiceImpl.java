package com.java.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.exam.entity.UserRole;
import com.java.exam.mapper.UserRoleMapper;
import com.java.exam.service.UserRoleService;
import com.java.exam.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

    private final RedisUtil redisUtil;

    @Override
    public String getMenuInfo(Integer roleId) {

        String menuInfo = (String) redisUtil.get("role_menu" + roleId);

        if (!StringUtils.hasText(menuInfo)) {
            menuInfo = userRoleMapper.selectOne(new QueryWrapper<UserRole>().eq("role_id", roleId)).getMenuInfo();
            redisUtil.set("role_menu" + roleId,menuInfo,1, TimeUnit.DAYS);
        }
        return menuInfo;
    }

    @Override
    public List<UserRole> getUserRole() {
        return userRoleMapper.selectList(null);
    }
}
