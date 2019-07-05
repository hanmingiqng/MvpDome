package com.han.mvpdome.view.inter;

public interface IMian1FView extends ActivityView {
    //响应标记
    int RESPONSE_ONE = 0;
    int RESPONSE_TWO = 1;
    int RESPONSE_THREE = 2;


    <T> void response(T response, int responseFlag);
}
