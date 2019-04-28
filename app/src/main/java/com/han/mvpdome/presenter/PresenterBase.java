package com.han.mvpdome.presenter;

import android.content.Context;

import com.han.mvpdome.httpUtils.ApiWrapper;
import com.han.mvpdome.model.ModerBase;
import com.han.mvpdome.model.impl.MainAModelImpl;
import com.han.mvpdome.presenter.inter.IMainAPresenter;
import com.han.mvpdome.view.inter.ActivityView;
import com.han.mvpdome.view.inter.IMainAView;

/**
 * Created on 2019-4-28  17:12
 *
 * @name hanmingqing
 * @user hanmq
 */
public class PresenterBase {
    public ModerBase moderBase;
    public Context mContext;

    public PresenterBase(Context mContext) {
        this.mContext = mContext;
    }


    public void setModerBase(ModerBase moderBase) {
        this.moderBase = moderBase;
    }

    public void dismissProgressDialog() {
        if (moderBase != null) {
            moderBase.dismissProgressDialog();
        }

    }
}
