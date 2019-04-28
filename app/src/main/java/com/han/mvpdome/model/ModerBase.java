package com.han.mvpdome.model;

import android.content.Context;

import com.han.mvpdome.beans.ErrorEvent;
import com.han.mvpdome.customview.CustomProgressDialog;
import com.han.mvpdome.httpUtils.ApiWrapper;
import com.han.mvpdome.httpUtils.RetrofitUtil;
import com.han.mvpdome.utils.HttpUtils;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 2019-4-26  18:31
 *
 * @name hanmingqing
 * @user hanmq
 */
public class ModerBase {
    public ApiWrapper apiWrapper;
    Context mContext;
    private static final String TAG = "ModerBase";
    public CompositeSubscription mCompositeSubscription;

    public ModerBase(Context mContext) {
        this.mContext = mContext;
        apiWrapper = new ApiWrapper();
        mCompositeSubscription = new CompositeSubscription();
    }

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

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
