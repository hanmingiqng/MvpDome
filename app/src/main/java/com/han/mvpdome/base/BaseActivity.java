package com.han.mvpdome.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.han.mvpdome.R;
import com.han.mvpdome.beans.ErrorEvent;
import com.han.mvpdome.customview.CustomProgressDialog;
import com.han.mvpdome.httpUtils.ApiWrapper;
import com.han.mvpdome.httpUtils.HttpUtil;
import com.han.mvpdome.httpUtils.RetrofitUtil;
import com.han.mvpdome.inter.PermissionsBase;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.utils.AppManager;
import com.han.mvpdome.utils.HttpUtils;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.utils.StatusBarUtils;
import com.han.mvpdome.utils.ToastUtil;
import com.han.mvpdome.view.inter.ActivityView;
import com.han.mvpdome.view.inter.IMainAView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * 活动基类
 */
public abstract class BaseActivity<P extends PresenterBase> extends AppCompatActivity implements ActivityView {

    protected BaseActivity mContext;

    private static final String TAG = "BaseActivity";
    public ApiWrapper apiWrapper = null;
    public FragmentManager fragmentManager;
    public Unbinder mUnbinder = null;
    public P presenterBase;

    /**
     * 获取加载View的ID
     */
    public abstract int getLayoutId();

    /**
     * 使用CompositeSubscription来持有所有的Subscriptions
     */
    public CompositeSubscription mCompositeSubscription;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initSaveInstance(savedInstanceState);
        AppManager.getAppManager().addActivity(this);

        Logger.d(TAG, "onCreate: " + getClass().getSimpleName());//打印当前活动名

        setContentView(getLayoutId());
//        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());

        mUnbinder = ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        apiWrapper = new ApiWrapper();
        mCompositeSubscription = new CompositeSubscription();

        StatusBarUtils.statusBarSetting(mContext);

        if (haveEventBus()) {
            EventBus.getDefault().register(this);
        }
//        初始化Presenter层
        presenterBase = getPresenterBase();
        presenterBase.registerView(this);
        initView();
        initData();
    }

    RxPermissions rxPermissions;

    public void requestEachCombined(final PermissionsBase permissionsBase, String... permissions) {
        if (rxPermissions == null) {
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
                            ToastUtil.showShortToast(mContext, "请求权限失败");
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
            dialog = new AlertDialog.Builder(this)
                    .setTitle(mTitle)//设置对话框的标题
                    .setMessage(mRationale)//设置对话框的内容
                    //设置对话框的按钮
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtil.showShortToast(mContext, "只有给予该权限,才能正常使用");
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            跳转到设置权限页面
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(0);
                            startActivityForResult(intent, 7534);
                        }
                    }).create();
        }
        dialog.show();
    }

    /**
     * 初始化Presenter层
     *
     * @return
     */
    public abstract P getPresenterBase();

    /**
     * 初始化页面
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    protected void initSaveInstance(Bundle savedInstanceState) {
    }

    /**
     * 是否注册EventBus
     */
    public abstract boolean haveEventBus();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (haveEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        //一旦调用了 CompositeSubscription.unsubscribe()，这个CompositeSubscription对象就不可用了,
        // 如果还想使用CompositeSubscription，就必须在创建一个新的对象了。
//        这句话必须加 要不然容易造成内存泄漏
        mCompositeSubscription.unsubscribe();
        dismissProgressDialog();
        if (presenterBase != null) {
            //Activity销毁时的调用，让具体实现BasePresenter中onViewDestroy()方法做出决定
            presenterBase.destroy();
        }
//        取消网络加载中等待框
//        if (presenterBase != null) {
//            presenterBase.dismissProgressDialog();
//        }
    }

    //设置activity沉浸式状态栏
    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
                //window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                //                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }


    //失败回调吐司
    @Override
    public <T> void showToast(String e) {
        dismissProgressDialog();
        if (!HttpUtils.isNetWorkAvailable(mContext)) {
            ToastUtil.showShortToast(this, "网络异常，请检查您的网络...");
        } else {
            ToastUtil.showShortToast(this, e);
        }
    }

    //显示等待框
    public void showProgressDialog() {
        showProgressDialog("请稍后。。。");
    }

    //转圈圈dialog
    public CustomProgressDialog progressDialog;

    public void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(mContext);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    //取消等待框
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }


    //轮播图
//    public static class BannerViewHolder implements MZViewHolder<BannerList.DataBean> {
//        private ImageView mImageView;
//
//        @Override
//        public View createView(Context context) {
//            // 返回页面布局
//            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
//            mImageView = (ImageView) view.findViewById(R.id.banner_image);
//            ScreenAdapterTools.getInstance().loadView(view);
//            return view;
//        }
//
//        @Override
//        public void onBind(final Context context, final int position, BannerList.DataBean data) {
//            // 数据绑定
//            Glide.with(context)
//                    .load(data.getPictureUrl())
//                    .centerCrop()
//                    .placeholder(R.drawable.default_icon)
//                    .into(mImageView);
//            if (data.getArticle() != null) {
//                mImageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //                    Intent intet = new Intent(context, RecommendedRouteDetailActivity.class);
//                        //                    context.startActivity(intet);
//                        RecommendLineList.DataBean detail = new RecommendLineList.DataBean();
//                        detail.setContentUrl(data.getArticle().getContentUrl());
//                        detail.setCoverImage(data.getArticle().getCoverImage());
//                        detail.setTitle(data.getArticle().getTitle());
//                        detail.setDescription(data.getArticle().getDescription());
//                        detail.setId(data.getArticle().getId());
//                        detail.setTypeId(data.getArticle().getTypeId());
//
//                        Intent intent = new Intent(context, RecommendedRouteDetailActivity.class);
//                        intent.putExtra("detail", detail);
//                        intent.putExtra("type", "3");
////                    intent.putExtra("typeId", dataBean.getTypeId()+"");
//                        context.startActivity(intent);
//                    }
//                });
//            }
//        }
//    }
}