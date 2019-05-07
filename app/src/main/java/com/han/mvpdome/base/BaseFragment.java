package com.han.mvpdome.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.han.mvpdome.httpUtils.ApiWrapper;
import com.han.mvpdome.inter.PermissionsBase;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.utils.StatusBarUtils;
import com.han.mvpdome.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

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

    RxPermissions rxPermissions;

    public void requestEachCombined(final PermissionsBase permissionsBase, String... permissions) {
        if (rxPermissions==null){
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions.requestEachCombined(permissions).
                subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            permissionsBase.isOK();
                            // 用户已经同意该权限
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            ToastUtil.showShortToast(activity, "请求权限失败");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            AlertDialogShow();
                        }
                    }
                });
    }

    AlertDialog dialog;

    //提示跳转到设置
    public void AlertDialogShow() {
        if (dialog == null) {
            String mRationale = "如果没有请求的权限，此应用程序可能无法正常工作,打开app设置界面修改app权限。";
            String mTitle = "权限要求";
            dialog = new AlertDialog.Builder(activity)
                    .setTitle(mTitle)//设置对话框的标题
                    .setMessage(mRationale)//设置对话框的内容
                    //设置对话框的按钮
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtil.showShortToast(activity, "只有给予该权限,才能正常使用");
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            跳转到设置权限页面
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.fromParts("package", activity.getPackageName(), null));
                            intent.addFlags(0);
                            startActivityForResult(intent, 7534);
                        }
                    }).create();
        }
        dialog.show();
    }

}
