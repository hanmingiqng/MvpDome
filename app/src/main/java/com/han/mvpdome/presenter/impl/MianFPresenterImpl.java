package com.han.mvpdome.presenter.impl;

import android.content.Context;

import com.han.mvpdome.model.impl.MianFModelImpl;
import com.han.mvpdome.model.inter.IMianFModel;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.inter.IMianFPresenter;
import com.han.mvpdome.view.inter.IMianFView;

public class MianFPresenterImpl extends PresenterBase implements IMianFPresenter {
    private IMianFView mIMianFView;
    private IMianFModel mIMianFModel;

    public MianFPresenterImpl(Context mContext,  IMianFView fIMianFView) {
        super(mContext);
        mIMianFView = fIMianFView;
        mIMianFModel = new MianFModelImpl();
    }
}
