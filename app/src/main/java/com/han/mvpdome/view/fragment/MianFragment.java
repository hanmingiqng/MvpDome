package com.han.mvpdome.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.han.mvpdome.R;
import com.han.mvpdome.base.BaseFragment;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.impl.MianFPresenterImpl;
import com.han.mvpdome.presenter.inter.IMianFPresenter;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.view.inter.IMianFView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 2019-5-5  17:23
 *
 * @name hanmingqing
 * @user hanmq
 */
public class MianFragment extends BaseFragment implements IMianFView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;
    private String data;
    private IMianFPresenter mIMianFPresenter;

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
        return new MianFPresenterImpl(activity, this);
    }

    @Override
    public boolean isLazyLoading() {
        return true;
    }

    @Override
    public boolean isonPauseView() {
        return false;
    }


    @Override
    public <T> T request(int requestFlag) {
        return null;
    }

    @Override
    public <T> void response(T response, int responseFlag) {

    }
}
