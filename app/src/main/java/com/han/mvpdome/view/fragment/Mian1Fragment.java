package com.han.mvpdome.view.fragment;

import android.widget.TextView;

import com.han.mvpdome.R;
import com.han.mvpdome.base.BaseFragment;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.inter.IMian1FPresenter;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.view.inter.IBaseView;

import butterknife.BindView;

/**
 * Created on 2019-5-5  17:23
 *
 * @name hanmingqing
 * @user hanmq
 */
public class Mian1Fragment extends BaseFragment  {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String data;
    private IMian1FPresenter mIMian1FPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView() {
        data = getArguments().getString("data");
        tvTitle.setText(data);
    }

    @Override
    public void initData() {
        Logger.e(TAG, "initData: " + tvTitle + getClass().getSimpleName());//打印当前活动名

    }

    @Override
    public boolean haveEventBus() {
        return false;
    }

    @Override
    public PresenterBase getPresenterBase() {
        return null;
    }

    @Override
    public boolean isLazyLoading() {
        return true;
    }

    @Override
    public boolean isonPauseView() {
        return true;
    }


    @Override
    public <T> void response(T response, int responseFlag) {

    }
}
