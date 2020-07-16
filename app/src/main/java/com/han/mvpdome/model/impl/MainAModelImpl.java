package com.han.mvpdome.model.impl;

import com.han.mvpdome.base.BaseAction1;
import com.han.mvpdome.beans.http.Response;
import com.han.mvpdome.model.ModelBase;
import com.han.mvpdome.model.inter.IMainAModel;
import com.han.mvpdome.presenter.callback.CallBack;

import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainAModelImpl extends ModelBase implements IMainAModel {


    @Override
    public void getList(Map<String, String> map, final CallBack callBack) {
//        apiWrapper 被观察者
        Subscription subscription = apiWrapper
                .getBank(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                观察者
                .subscribe(newSubscriber(new BaseAction1<Response>() {

                    @Override
                    public void onError(String t) {
                        callBack.onError(t);
                    }

                    @Override
                    public void call(Response dataBean) {
                        if (dataBean.success) {
//                            解析成
                            callBack.onSuccess(dataBean.data);

                        } else {
                            callBack.onError(dataBean.message);
                        }
                    }
                }));
    }
}
