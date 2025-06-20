package com.java.exam.loggin;


import cn.hutool.json.JSONUtil;
import com.java.exam.entity.Logging;
import com.java.exam.service.LoggingService;
import com.java.exam.utils.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class LogAspect {
    @Autowired
    private LoggingService loggingService;//我自己定义的保存日志的dao

    //切点就是被Log注释的方法
    @Pointcut("execution(public * com.java.exam.controller.*.*(..))  && @annotation(com.java.exam.loggin.Log)")
    public void logPointCut() {}

    //方法执行前后都操作
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        saveSysLog(point, time);
        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Logging sysLog = new Logging();
        Log syslog = method.getAnnotation(Log.class);
        if(syslog != null){
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try{
            sysLog.setParams(JSONUtil.toJsonStr(args));
        }catch (Exception ignored){
        }

        //用户名
        sysLog.setCreateUser(SecurityUtils.getUsername());
        sysLog.setTime(time);
        sysLog.setCreateTime(new Date());
        //保存系统日志
        loggingService.save(sysLog);
    }
}
