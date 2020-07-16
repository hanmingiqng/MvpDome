package com.han.mvpdome.view.inter;

/**
 * Created on 2019-4-28  17:42
 *
 * @name hanmingqing
 * @user hanmq
 */
public interface IBaseView {
    //    失败回调
    <T> void showToast(String e);

    int RESPONSE_ONE = 0;
    int RESPONSE_TWO = 1;
    int RESPONSE_THREE = 2;

    //成功
    <T> void response(T response, int responseFlag);
}
