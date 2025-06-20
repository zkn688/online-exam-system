package com.java.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.exam.entity.Logging;
import com.java.exam.exception.BaseException;
import com.java.exam.exception.ErrorCode;
import com.java.exam.mapper.LoggingMapper;
import com.java.exam.service.LoggingService;
import com.java.exam.utils.PageResponse;
import com.java.exam.utils.QueryWrapperUtils;
import com.java.exam.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoggingServiceImpl extends ServiceImpl<LoggingMapper, Logging> implements LoggingService {

    private final LoggingMapper loggingMapper;


    @Override
    public PageResponse<Logging> getAllLogging(String content, Integer pageNo, Integer pageSize) {

        IPage<Logging> loggingIPage = new Page<>(pageNo, pageSize);
        QueryWrapper<Logging> wrapper = new QueryWrapper<>();
        QueryWrapperUtils.setLikeWrapper(wrapper, Collections.singletonMap("operation", content));
        wrapper.orderByDesc("create_time");

        loggingIPage = loggingMapper.selectPage(loggingIPage, wrapper);

        return PageResponse.<Logging>builder().data(loggingIPage.getRecords()).total(loggingIPage.getTotal()).build();
    }

    @Override
    public void deleteLoggingByIds(String loggingIds) {
        if (SecurityUtils.getUserId() != 1){
            throw new BaseException(ErrorCode.FORBIDDEN);
        }
        String[] ids = loggingIds.split(",");
        loggingMapper.deleteBatchIds(Arrays.asList(ids));
    }

}
