package com.han.mvpdome.model;

import com.han.mvpdome.base.BaseAction1;
import com.han.mvpdome.beans.http.ErrorEvent;
import com.han.mvpdome.httpUtils.retrofit.ApiWrapper;
import com.han.mvpdome.httpUtils.retrofit.RetrofitUtil;
import com.han.mvpdome.utils.Logger;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 2019-4-26  18:31
 *
 * @name hanmingqing
 * @user hanmq
 */
public class ModelBase {
    public ApiWrapper apiWrapper;
    private static final String TAG = "ModelBase";
    public CompositeSubscription mCompositeSubscription;

    public ModelBase() {
        apiWrapper = new ApiWrapper();
        mCompositeSubscription = new CompositeSubscription();
    }


    /**
     * 创建观察者
     *
     * @param onNext
     * @param <T>
     * @return
     */
    public <T> Subscriber newSubscriber(final BaseAction1<? super T> onNext) {
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
                    onNext.onError(exception.message);
                } else if (e instanceof SocketTimeoutException) {
                    onNext.onError("链接超时");
                } else if (e instanceof ConnectException) {
                    onNext.onError(e.getMessage());
                } else if (e instanceof HttpException) {

                    HttpException exception = (HttpException) e;
                    String message = exception.response().message();
                    int code = exception.response().code();
                    if (code == 500) {
                    }
                }
                onNext.onError("");
                Logger.e(TAG, String.valueOf(e.getMessage()) + ".......");
                EventBus.getDefault().post(new ErrorEvent("Error"));
            }

            @Override
            public void onNext(T t) {
                if (!mCompositeSubscription.isUnsubscribed()) {
                    onNext.call(t);
                }
            }

        };
    }

}
