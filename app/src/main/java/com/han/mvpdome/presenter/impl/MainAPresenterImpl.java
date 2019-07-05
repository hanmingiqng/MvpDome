package com.han.mvpdome.presenter.impl;

import android.view.View;

import com.han.mvpdome.httpUtils.Response;
import com.han.mvpdome.model.ModelBase;
import com.han.mvpdome.model.impl.MainAModelImpl;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.callback.CallBack;
import com.han.mvpdome.presenter.inter.IMainAPresenter;
import com.han.mvpdome.view.inter.IMainAView;

import java.util.Map;

public class MainAPresenterImpl extends PresenterBase<IMainAView, MainAModelImpl> implements IMainAPresenter {

    public MainAPresenterImpl() {
        super();

    }

    @Override
    public void getList(Map<String, String> map) {
        model.getList(map, new CallBack<Response>() {
            @Override
            public void onSuccess(Response response) {
                if (getView() != null) {
//                    操作数据
                    getView().response("成功", getView().RESPONSE_ONE);
                }

            }

            @Override
            public void onError(String t) {
                if (getView() != null) {
                    getView().showToast(t);
                }
            }
        });
    }

    @Override
    public MainAModelImpl getModelBase() {
        return new MainAModelImpl();
    }


}
