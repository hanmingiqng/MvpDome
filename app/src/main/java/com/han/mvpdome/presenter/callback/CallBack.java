package com.han.mvpdome.presenter.callback;

public interface CallBack<T> {
    void onSuccess(T response);

    void onError(String t);
}
