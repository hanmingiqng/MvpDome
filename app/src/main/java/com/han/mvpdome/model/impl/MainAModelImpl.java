package com.han.mvpdome.model.impl;

import android.content.Context;

import com.han.mvpdome.customview.CustomProgressDialog;
import com.han.mvpdome.httpUtils.ApiWrapper;
import com.han.mvpdome.httpUtils.Response;
import com.han.mvpdome.model.ModerBase;
import com.han.mvpdome.model.inter.IMainAModel;
import com.han.mvpdome.presenter.callback.CallBack;
import com.han.mvpdome.utils.ToastUtil;

import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainAModelImpl extends ModerBase implements IMainAModel {

    public MainAModelImpl(Context mContext) {
        super(mContext);
    }


    @Override
    public void getList(Map<String, String> map, final CallBack callBack) {
        Subscription subscription = apiWrapper
                .getBank(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newSubscriber(new Action1<Response>() {
                    @Override
                    public void call(Response dataBean) {
                        dismissProgressDialog();
                        if (dataBean.success) {
                            callBack.onSuccess("成");

                        } else {
                            callBack.onError(dataBean.message);
                        }
                    }
                }));
    }
}
