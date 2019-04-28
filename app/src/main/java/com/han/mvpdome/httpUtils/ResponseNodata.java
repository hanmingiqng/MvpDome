package com.han.mvpdome.httpUtils;

/**
 * 描述：基础的数据封装类
 */

public class ResponseNodata {

    public boolean success;
    public String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

