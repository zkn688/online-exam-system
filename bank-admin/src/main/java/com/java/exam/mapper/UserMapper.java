package com.java.exam.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.exam.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
