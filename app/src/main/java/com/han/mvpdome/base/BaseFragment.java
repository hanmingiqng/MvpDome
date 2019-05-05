package com.han.mvpdome.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.han.mvpdome.httpUtils.ApiWrapper;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liangleixin on 2016/3/18.
 */
public abstract class BaseFragment extends Fragment {
    protected BaseActivity activity;
    public ApiWrapper apiWrapper = null;

    private Unbinder mUnbinder = null;
    /**
     * p层
     */
    private PresenterBase presenterBase;

    /**
     * 获取加载View的ID
     */
    public abstract int getLayoutId();

    /**
     * 实例化控件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();


    /**
     * 是否注册EventBus
     */
    public abstract boolean haveEventBus();

    /**
     * 初始化Presenter层
     *
     * @return
     */
    public abstract PresenterBase getPresenterBase();

    /**
     *懒加载
     */
    /**
     * 是否开启懒加载
     */
    public abstract boolean isLazyLoading();

    private boolean isViewCreated; // 界面是否已创建完成
    private boolean isVisibleToUser; // 是否对用户可见
    private boolean isDataLoaded; // 数据是否已请求
    public abstract boolean isonPauseView(); // 是否开启 返回刷新数据
    public static final String TAG = "BaseFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);  //这里影响了getLayout()布局垂直方向的matchparent  铺不满屏幕   我以前写的是  inflate(getlayout(),null)
//        ScreenAdapterTools.getInstance().loadView(view);
        apiWrapper = new ApiWrapper();

        mUnbinder = ButterKnife.bind(this, view);
        activity = (BaseActivity) getActivity();

        initView();


        StatusBarUtils.statusBarSetting(activity);

        if (haveEventBus()) {
            EventBus.getDefault().register(this);

        }
//        初始化p层
        presenterBase = getPresenterBase();
        isViewCreated = true;
//        判断是否开启懒加载
        if (!isLazyLoading()) {
            initData();
        } else {
            tryLoadData();
        }
        return view;

    }

    /**
     *
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
//        判断是否是显示
        tryLoadData();
    }

    /**
     * 判断是否是显示
     */
    public void tryLoadData() {
        Log.e(TAG, "tryLoadData: 界面是否已创建完成 " + isViewCreated + "------是否对用户可见" + isVisibleToUser + "------数据是否已请求" + isDataLoaded);//打印当前活动名
        if (isViewCreated && isVisibleToUser && !isDataLoaded && isLazyLoading()) {

            initData();
            isDataLoaded = true;
            super.onStart();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        tryLoadData();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        if (isonPauseView()){
            isDataLoaded = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        apiWrapper.cancelcall();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (haveEventBus()) {
            EventBus.getDefault().unregister(this);
            EventBus.getDefault().removeAllStickyEvents();
        }

    }


}
