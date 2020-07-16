package com.han.mvpdome.presenter.impl;

import com.han.mvpdome.model.impl.Mian1FModelImpl;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.inter.IMian1FPresenter;
import com.han.mvpdome.view.inter.IBaseView;

public class Mian1FPresenterImpl extends PresenterBase<IBaseView,Mian1FModelImpl> implements IMian1FPresenter {

    public Mian1FPresenterImpl() {
    }

    @Override
    public Mian1FModelImpl getModelBase() {
        return new Mian1FModelImpl();
    }
}
