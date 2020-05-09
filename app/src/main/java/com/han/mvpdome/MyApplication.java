package com.han.mvpdome;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.bumptech.glide.request.target.ViewTarget;
import com.han.mvpdome.utils.CrashHandler;
import com.han.mvpdome.utils.SpUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.tinker.entry.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;

import io.reactivex.annotations.NonNull;

/**
 * xmind
 *
 * @author liyuanli
 * @data 2017/11/28
 */

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    private static MyApplication mInstance;
    private static Context context;//公共的上下文变量

    private SpUtil spUtil;

    public Vibrator mVibrator;
    private ApplicationLike tinkerApplicationLike;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        FrescoImageLoader.init(this);
////        检查内存泄漏
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
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
//        热修复
        initTinkerPatch();

    }


    /**
     * 我们需要确保至少对主进程跟patch进程初始化 TinkerPatch
     */
    private void initTinkerPatch() {
        // 我们可以从这里获得Tinker加载过程的信息
        if (BuildConfig.TINKER_ENABLE) {
            tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
            // 初始化TinkerPatch SDK
            TinkerPatch.init(
                    tinkerApplicationLike
//                new TinkerPatch.Builder(tinkerApplicationLike)
//                    .requestLoader(new OkHttp3Loader())
//                    .build()
            )
                    .reflectPatchLibrary()
                    .setPatchRollbackOnScreenOff(true)
                    .setPatchRestartOnSrceenOff(true)
                    .setFetchPatchIntervalByHours(3)
            ;
            // 获取当前的补丁版本
            Log.d(TAG, "Current patch version is " + TinkerPatch.with().getPatchVersion());

            // fetchPatchUpdateAndPollWithInterval 与 fetchPatchUpdate(false)
            // 不同的是，会通过handler的方式去轮询
            TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
        }
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
