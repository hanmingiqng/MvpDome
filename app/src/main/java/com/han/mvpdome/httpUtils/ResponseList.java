package com.han.mvpdome.httpUtils;

/**
 * 描述：基础的数据封装类
 */

public class ResponseList<T> {

    public boolean success;
    public String message;
    public T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

