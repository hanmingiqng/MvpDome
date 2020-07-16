package com.han.mvpdome.presenter.impl;

import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.inter.IMain4APresenter;
import com.han.mvpdome.model.impl.Main4AModelImpl;

import com.han.mvpdome.view.inter.IMain4AView;

public class Main4APresenterImpl extends PresenterBase<IMain4AView, Main4AModelImpl> implements IMain4APresenter {

    public Main4APresenterImpl() {
        super();
    }

    @Override
    public Main4AModelImpl getModelBase() {
        return new Main4AModelImpl();
    }
}
