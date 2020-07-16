package com.han.mvpdome.view.fragment;

import android.util.Log;
import android.widget.TextView;

import com.han.mvpdome.R;
import com.han.mvpdome.Text;
import com.han.mvpdome.base.BaseFragment;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.impl.MianFPresenterImpl;
import com.han.mvpdome.presenter.inter.IMianFPresenter;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.utils.ToastUtil;
import com.han.mvpdome.view.inter.IBaseView;

import butterknife.BindView;

/**
 * Created on 2019-5-5  17:23
 *
 * @name hanmingqing
 * @user hanmq
 */
public class MianFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
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
        mIMianFPresenter = (IMianFPresenter) presenterBase;
    }

    @Override
    public void initData() {
        Logger.e(TAG, "initData: " + tvTitle + getClass().getSimpleName());//打印当前活动名
        Log.e("name", "OnClick: " + Text.name);
        ToastUtil.showShortToast(activity, Text.name);

    }

    @Override
    public boolean haveEventBus() {
        return false;
    }

    @Override
    public PresenterBase getPresenterBase() {
        return new MianFPresenterImpl();
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
    public <T> void response(T response, int responseFlag) {

    }
}
