package com.han.mvpdome.beans;

/**
 * @author liyuanli
 * @date 2018/7/10
 */

public class ErrorEvent {
    private String param;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public ErrorEvent(String param) {
        this.param = param;
    }
}
