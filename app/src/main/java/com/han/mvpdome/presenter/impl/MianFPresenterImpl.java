package com.han.mvpdome.presenter.impl;

import android.content.Context;

import com.han.mvpdome.model.ModelBase;
import com.han.mvpdome.model.impl.MainAModelImpl;
import com.han.mvpdome.model.impl.MianFModelImpl;
import com.han.mvpdome.model.inter.IMianFModel;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.inter.IMianFPresenter;
import com.han.mvpdome.view.inter.IMainAView;
import com.han.mvpdome.view.inter.IMianFView;

public class MianFPresenterImpl extends PresenterBase<IMianFView, MianFModelImpl> implements IMianFPresenter {

    public MianFPresenterImpl() {
        super();
    }


    @Override
    public MianFModelImpl getModelBase() {
        return new MianFModelImpl();
    }
}
