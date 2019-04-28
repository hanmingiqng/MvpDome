package com.han.mvpdome.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.han.mvpdome.httpUtils.ApiWrapper;
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
     * 获取加载View的ID
     */
    public abstract boolean haveEventBus();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);  //这里影响了getLayout()布局垂直方向的matchparent  铺不满屏幕   我以前写的是  inflate(getlayout(),null)
//        ScreenAdapterTools.getInstance().loadView(view);
        apiWrapper = new ApiWrapper();

        mUnbinder = ButterKnife.bind(this, view);
        activity = (BaseActivity) getActivity();

        initView();
        initData();

        StatusBarUtils.statusBarSetting(activity);

        if (haveEventBus()) {
            EventBus.getDefault().register(this);

        }
        return view;

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


    //    //转圈圈dialog
    //    private CustomProgressDialog progressDialog;
    //
    //    public void showProgressDialog() {
    //        showProgressDialog("");
    //    }
    //
    //    public void showProgressDialog(String message) {
    //        if (progressDialog == null) {
    //            progressDialog = CustomProgressDialog.createDialog(getActivity());
    //            progressDialog.setCanceledOnTouchOutside(false);
    //        }
    //        progressDialog.setMessage(message);
    //        progressDialog.show();
    //    }
    //
    //    public void dismissProgressDialog() {
    //        if (progressDialog != null && progressDialog.isShowing())
    //            progressDialog.dismiss();
    //    }

}
