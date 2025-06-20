package com.java.exam.exception;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 5565760508056698922L;

    private final Integer code;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.code = errorCode.getCode();
    }

    public BaseException(Integer code,String msg) {
        super(msg);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
