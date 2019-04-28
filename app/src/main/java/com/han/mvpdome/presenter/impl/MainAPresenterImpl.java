package com.han.mvpdome.presenter.impl;

import android.content.Context;

import com.han.mvpdome.customview.CustomProgressDialog;
import com.han.mvpdome.httpUtils.ApiWrapper;
import com.han.mvpdome.model.ModerBase;
import com.han.mvpdome.model.impl.MainAModelImpl;
import com.han.mvpdome.model.inter.IMainAModel;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.callback.CallBack;
import com.han.mvpdome.presenter.inter.IMainAPresenter;
import com.han.mvpdome.view.inter.ActivityView;
import com.han.mvpdome.view.inter.IMainAView;

import java.util.Map;

public class MainAPresenterImpl<T> extends PresenterBase implements IMainAPresenter {
    private MainAModelImpl mainAModel;
    private IMainAView iMainAView;

    public MainAPresenterImpl(Context mContext, IMainAView iMainAView) {
        super(mContext);
        this.iMainAView = iMainAView;
        mainAModel = new MainAModelImpl(mContext);
        setModerBase(mainAModel);
    }

    @Override
    public void getList(Map<String, String> map) {
        mainAModel.getList(map, new CallBack<T>() {
            @Override
            public void onSuccess(Object response) {
                if (iMainAView != null) {
                    iMainAView.request(1);
                }

            }

            @Override
            public void onError(String t) {
                if (iMainAView != null) {
                    iMainAView.request1(t);
                }
            }
        });
    }


}
