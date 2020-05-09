package com.han.mvpdome.view.activity;

import android.content.IntentFilter;
import android.util.Log;

import com.han.mvpdome.R;
import com.han.mvpdome.Text;
import com.han.mvpdome.base.BaseActivity;
import com.han.mvpdome.presenter.impl.MainAPresenterImpl;
import com.han.mvpdome.utils.ToastUtil;

import butterknife.OnClick;

public class Main2Activity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainAPresenterImpl getPresenterBase() {
        return new MainAPresenterImpl();
//        "https://imtt.dd.qq.com/16891/apk/FD7D6F00C79DCD0F24B3B54681CAC968.apk"

    }

    @Override
    public void initView() {
        Log.e("name", Text.name);
    }

    @OnClick(R.id.btn)
    public void OnClick() {
        ToastUtil.showShortToast(mContext, "11");
//        //开启广播
//        //创建一个意图对象
//        Intent intent = new Intent();
//        //指定发送广播的频道
//        intent.setAction("android.provider.Telephony.SMS_RECEIVED");
//        //发送广播的数据
//        intent.putExtra("key", "发送无序广播,顺便传递的数据");
//        //发送
//        sendBroadcast(intent);

//        PackageManager packageManager = this.getPackageManager();
//        Intent intent= packageManager.getLaunchIntentForPackage("com.alibaba.android.rimet");
//        startActivity(intent);
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean haveEventBus() {
        return false;
    }


}
