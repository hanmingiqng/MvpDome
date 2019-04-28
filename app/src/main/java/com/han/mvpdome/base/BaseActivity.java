package com.han.mvpdome.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.utils.AppManager;
import com.han.mvpdome.utils.HttpUtils;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.utils.StatusBarUtils;
import com.han.mvpdome.utils.ToastUtil;
import com.han.mvpdome.view.inter.IMainAView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * 活动基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected BaseActivity mContext;

    private static final String TAG = "BaseActivity";
    public ApiWrapper apiWrapper = null;
    public FragmentManager fragmentManager;
    public Unbinder mUnbinder = null;
    public PresenterBase presenterBase;

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
        initData();
    }

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

    /**
     * 创建观察者
     *
     * @param onNext
     * @param <T>
     * @return
     */
    public <T> Subscriber newSubscriber(final Action1<? super T> onNext) {
        return new Subscriber<T>() {
            @Override
            public void onStart() {
                super.onStart();
                //showProgressDialog();
            }

            @Override
            public void onCompleted() {
                //dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof RetrofitUtil.APIException) {
                    RetrofitUtil.APIException exception = (RetrofitUtil.APIException) e;
                    ToastUtil.showShortToast(mContext, exception.message);
                } else if (e instanceof SocketTimeoutException) {
                    ToastUtil.showShortToast(mContext, "链接超时");
                } else if (e instanceof ConnectException) {
                    ToastUtil.showShortToast(mContext, e.getMessage());
                } else if (e instanceof HttpException) {

                    HttpException exception = (HttpException) e;
                    String message = exception.response().message();
                    int code = exception.response().code();
                    if (code == 500) {
//                        ToastUtil.showShortToast(mContext, "网络异常");
                    }
                }
                if (!HttpUtils.isNetWorkAvailable(mContext)) {
                    ToastUtil.showShortToast(mContext, "网络异常，请检查您的网络...");
                }
                Logger.e(TAG, String.valueOf(e.getMessage()) + ".......");
                EventBus.getDefault().post(new ErrorEvent("Error"));

                dismissProgressDialog();
            }

            @Override
            public void onNext(T t) {
                if (!mCompositeSubscription.isUnsubscribed()) {
                    onNext.call(t);
                }
            }

        };
    }

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
//        取消网络加载中等待框
        if (presenterBase!=null){
            presenterBase.dismissProgressDialog();
        }
    }

    //转圈圈dialog
    public CustomProgressDialog progressDialog;

    public void showProgressDialog() {
        showProgressDialog("请稍后。。。");
    }

    public void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
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