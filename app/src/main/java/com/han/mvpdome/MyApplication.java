package com.han.mvpdome;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

import com.bumptech.glide.request.target.ViewTarget;
import com.han.mvpdome.utils.CrashHandler;
import com.han.mvpdome.utils.SpUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * xmind
 *
 * @author liyuanli
 * @data 2017/11/28
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static Context context;//公共的上下文变量

    private SpUtil spUtil;

    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        FrescoImageLoader.init(this);
        context = getApplicationContext();
        //尺寸适配
//        ScreenAdapterTools.init(this);
        //sp初始化
        spUtil = SpUtil.getInstance(context);
        //log管理
        initLog();
        //glide
        ViewTarget.setTagId(R.id.glide_tag);
//
//        //地图
//        SDKInitializer.initialize(getApplicationContext(), "");
//
//        //友盟初始化
//        UMConfigure.init(this, "5a12384aa40fa3551f0001d1", "umeng",
//                UMConfigure.DEVICE_TYPE_PHONE, "");
        //每次用户第三方登录都需要重新授权
//        UMShareConfig config = new UMShareConfig();
//        config.isNeedAuthOnGetUserInfo(true);
//        UMShareAPI.get(mInstance).setShareConfig(config);
//设置全局异常捕获
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context);
    }

    {
//        PlatformConfig.setWeixin("wxc4305cfa36ca4412", "55c4884f219efba48782d9bb027e6caf");
//        //豆瓣RENREN平台目前只能在服务器端配置
//        PlatformConfig.setSinaWeibo("1212762106", "c6be7a1890d20e4ac2c97b16a612e031", "http://sns.whalecloud.com");
//        PlatformConfig.setQQZone("1106710593", "X45YmutJLSHfdsqG");
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static Context getApplication() {
        return context;
    }

    /**
     * 初始化日志输出工具类
     */
    private void initLog() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

}
