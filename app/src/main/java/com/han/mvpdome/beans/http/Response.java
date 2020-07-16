package com.han.mvpdome.beans.http;

/**
 * 描述：基础的数据封装类 返回对象数据
 */

public class Response<T> {

    public boolean success;
    public String message;
    public int  code;
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

