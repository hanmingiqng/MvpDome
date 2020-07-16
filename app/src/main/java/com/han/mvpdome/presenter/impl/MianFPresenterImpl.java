package com.han.mvpdome.presenter.impl;

import com.han.mvpdome.model.impl.MianFModelImpl;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.inter.IMianFPresenter;
import com.han.mvpdome.view.inter.IBaseView;

public class MianFPresenterImpl extends PresenterBase<IBaseView,MianFModelImpl> implements IMianFPresenter {

    public MianFPresenterImpl() {
        super();
    }


    @Override
    public MianFModelImpl getModelBase() {
        return new MianFModelImpl();
    }
}
