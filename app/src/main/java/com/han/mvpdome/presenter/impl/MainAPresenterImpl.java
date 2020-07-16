package com.han.mvpdome.presenter.impl;

import com.han.mvpdome.beans.http.Response;
import com.han.mvpdome.model.impl.MainAModelImpl;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.callback.CallBack;
import com.han.mvpdome.presenter.inter.IMainAPresenter;
import com.han.mvpdome.view.activity.MainActivity;
import com.han.mvpdome.view.inter.IBaseView;

import java.util.Map;

public class MainAPresenterImpl extends PresenterBase<IBaseView,MainAModelImpl> implements IMainAPresenter {

    public MainAPresenterImpl() {
        super();

    }

    @Override
    public void getList(Map<String, String> map, final int responseFlag) {
        CallBack callBack =  new CallBack<Object>() {
            @Override
            public void onSuccess(Object response) {
                if (getView() != null) {
//                    操作数据
                    getView().response("成功", responseFlag);

                }

            }
            @Override
            public void onError(String t) {
                if (getView() != null) {
                    getView().showToast(t);
                }
            }
        };
        model.getList(map, callBack);
    }


    @Override
    public MainAModelImpl getModelBase() {
        return new MainAModelImpl();
    }


}
