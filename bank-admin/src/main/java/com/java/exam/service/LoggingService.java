package com.java.exam.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.exam.entity.Logging;
import com.java.exam.utils.PageResponse;

public interface LoggingService extends IService<Logging> {

    PageResponse<Logging> getAllLogging(String content, Integer pageNo, Integer pageSize);

    void deleteLoggingByIds(String ids);
}
