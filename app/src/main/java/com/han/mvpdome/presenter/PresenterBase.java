package com.han.mvpdome.presenter;

import android.content.Context;
import android.view.View;

import com.han.mvpdome.customview.CustomProgressDialog;
import com.han.mvpdome.httpUtils.ApiWrapper;
import com.han.mvpdome.model.ModelBase;
import com.han.mvpdome.model.impl.MainAModelImpl;
import com.han.mvpdome.presenter.inter.IMainAPresenter;
import com.han.mvpdome.utils.HttpUtils;
import com.han.mvpdome.view.inter.ActivityView;
import com.han.mvpdome.view.inter.IMainAView;

import java.lang.ref.WeakReference;

/**
 * Created on 2019-4-28  17:12
 *
 * @name hanmingqing
 * @user hanmq
 */
public abstract class PresenterBase<V extends ActivityView, M extends ModelBase> {
    private WeakReference<V> wrf;
    public M model;

    public PresenterBase() {
        this.model = getModelBase();

    }

    //弱链接 设置回调
    public void registerView(V view) {
        wrf = new WeakReference<V>(view);
    }

    //软连接 回去回调
    public V getView() {
        return wrf == null ? null : wrf.get();
    }

    /**
     * 在Activity或Fragment卸载时调用View结束的方法
     */
    public void destroy() {
        if (wrf != null) {
            wrf.clear();
            wrf = null;
        }
    }

    public abstract M getModelBase();

}
