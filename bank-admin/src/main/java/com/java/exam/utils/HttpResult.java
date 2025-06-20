package com.java.exam.utils;

import com.java.exam.exception.ErrorCode;
import lombok.Data;

@Data
public class HttpResult {
    private Integer code;
    private String msg;
    private Object data;

    public static HttpResult success(){
        HttpResult httpRequest = new HttpResult();
        httpRequest.setCode(200);
        httpRequest.setMsg(null);
        httpRequest.setData(null);
        return httpRequest;
    }

    public static HttpResult success(Object data){
        HttpResult httpRequest = new HttpResult();
        httpRequest.setCode(200);
        httpRequest.setMsg(null);
        httpRequest.setData(data);
        return httpRequest;
    }

    public static HttpResult error(String msg, Integer code){
        HttpResult httpRequest = new HttpResult();
        httpRequest.setCode(code);
        httpRequest.setMsg(msg);
        httpRequest.setData(null);
        return httpRequest;
    }

    public static HttpResult error(ErrorCode errorCode){
        HttpResult httpRequest = new HttpResult();
        httpRequest.setCode(errorCode.getCode());
        httpRequest.setMsg(errorCode.getDesc());
        httpRequest.setData(null);
        return httpRequest;
    }


}
