package com.han.mvpdome.model.inter;

import com.han.mvpdome.presenter.callback.CallBack;

import java.util.Map;

public interface IMainAModel {
    void getList(Map<String, String> map, CallBack callBack);
}
