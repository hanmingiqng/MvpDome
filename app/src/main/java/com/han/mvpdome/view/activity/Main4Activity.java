package com.han.mvpdome.view.activity;

import com.han.mvpdome.base.BaseActivity;
import com.han.mvpdome.presenter.impl.Main4APresenterImpl;
import com.han.mvpdome.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.han.mvpdome.view.inter.IMain4AView;

public class Main4Activity extends BaseActivity<Main4APresenterImpl> implements IMain4AView {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main4;
    }

    @Override
    public Main4APresenterImpl getPresenterBase() {
        return new Main4APresenterImpl();

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public boolean haveEventBus() {
        return false;
    }

    //成功回调
    @Override
    public <T> void response(T response, int responseFlag) {
        dismissProgressDialog();

    }
}
