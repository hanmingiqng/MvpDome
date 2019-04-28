package com.han.mvpdome.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.han.mvpdome.R;
import com.han.mvpdome.base.BaseActivity;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.impl.MainAPresenterImpl;
import com.han.mvpdome.presenter.inter.IMainAPresenter;
import com.han.mvpdome.view.inter.IMainAView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements IMainAView {

    private MainAPresenterImpl mainAPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void initData() {
        mainAPresenter = new MainAPresenterImpl(this);
        //        设置回调
        mainAPresenter.setmIMainAView(this);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("checkCode", 111 + "");
        dataMap.put("loginName", "ceshi_han");
        dataMap.put("password ", "123456");
        mainAPresenter.getList(dataMap);
    }

    @Override
    public boolean haveEventBus() {
        return false;
    }

    @Override
    public <T> T request(int requestFlag) {
        Toast.makeText(this, "" + requestFlag, Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public <T> T request1(String requestFlag) {
        Toast.makeText(this, "" + requestFlag, Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public <T> void response(T response, int responseFlag) {

    }
}
