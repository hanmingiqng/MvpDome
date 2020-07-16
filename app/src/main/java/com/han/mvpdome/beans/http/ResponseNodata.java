package com.han.mvpdome.beans.http;

/**
 * 描述：基础的数据封装类  不返回数据
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

