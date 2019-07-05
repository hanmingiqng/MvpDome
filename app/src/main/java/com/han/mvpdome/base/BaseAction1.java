package com.han.mvpdome.base;

import rx.functions.Action1;

/**
 * Created on 2019-7-5  15:22
 *
 * @name hanmingqing
 * @user hanmq
 */
public interface BaseAction1<T> extends Action1<T> {
    void onError(String t);
}
