package com.han.mvpdome.model.impl;

import android.content.Context;

import com.han.mvpdome.base.BaseAction1;
import com.han.mvpdome.customview.CustomProgressDialog;
import com.han.mvpdome.httpUtils.ApiWrapper;
import com.han.mvpdome.httpUtils.Response;
import com.han.mvpdome.model.ModelBase;
import com.han.mvpdome.model.inter.IMainAModel;
import com.han.mvpdome.presenter.callback.CallBack;
import com.han.mvpdome.utils.HttpUtils;
import com.han.mvpdome.utils.ToastUtil;

import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainAModelImpl extends ModelBase implements IMainAModel {


    @Override
    public void getList(Map<String, String> map, final CallBack callBack) {
        Subscription subscription = apiWrapper
                .getBank(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newSubscriber(new BaseAction1<Response>() {

                    @Override
                    public void onError(String t) {
                        callBack.onError(t);
                    }

                    @Override
                    public void call(Response dataBean) {
                        if (dataBean.success) {
                            callBack.onSuccess("成");

                        } else {
                            callBack.onError(dataBean.message);
                        }
                    }
                }));
    }
}
