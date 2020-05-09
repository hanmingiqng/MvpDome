package com.han.mvpdome.view.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.TextView;

import com.han.mvpdome.MyApplication;
import com.han.mvpdome.R;
import com.han.mvpdome.Text;
import com.han.mvpdome.base.BaseActivity;
import com.han.mvpdome.base.BaseFragment;
import com.han.mvpdome.beans.DownloadEvent;
import com.han.mvpdome.inter.PermissionsBase;
import com.han.mvpdome.presenter.impl.MainAPresenterImpl;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.utils.ToastUtil;
import com.han.mvpdome.view.fragment.Mian1Fragment;
import com.han.mvpdome.view.fragment.Mian2Fragment;
import com.han.mvpdome.view.fragment.Mian3Fragment;
import com.han.mvpdome.view.fragment.MianFragment;
import com.han.mvpdome.view.inter.IMainAView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainAPresenterImpl> implements IMainAView {

    @BindView(R.id.vp_main)
    ViewPager vpMain;
    @BindView(R.id.tv_name)
    TextView tvName;
    private ArrayList<BaseFragment> fragmentList;
    private BaseFragment oneFragment, mian1Fragment, mian2Fragment, mian3Fragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainAPresenterImpl getPresenterBase() {
        return new MainAPresenterImpl();
//        "https://imtt.dd.qq.com/16891/apk/FD7D6F00C79DCD0F24B3B54681CAC968.apk"

    }

    @OnClick(R.id.btn)
    public void OnClick() {
//        tvName.setVisibility(View.VISIBLE);
//        TinkerPatch.with().fetchPatchUpdate(true);
//        Map<String, String> dataMap = new HashMap<>();
//        dataMap.put("checkCode", 111 + "");
//        dataMap.put("loginName", "ceshi_han");
//        dataMap.put("password ", "123456");
//        if (presenterBase != null) {
//            showProgressDialog();
//            presenterBase.getList(dataMap);
//        }
//        Intent intent = new Intent(this, DownloadService.class);
//        intent.putExtra("url", "https://imtt.dd.qq.com/16891/apk/FD7D6F00C79DCD0F24B3B54681CAC968.apk");
//        startService(intent);
        Intent starter = new Intent(mContext, Main2Activity.class);
        mContext.startActivity(starter);

//        new UpdateApkUtil(this, new UpdateApkinterface() {
//            @Override
//            public void isOK() {
//
//            }
//
//            @Override
//            public void isNo() {
//                ToastUtil.showShortToast(mContext, "取消更新");
//            }
//        }).updateApkUtil("1.1");

    }

    @Override
    public void initView() {
        Text.name = "123131";
        fragmentList = new ArrayList<BaseFragment>();
//        for (int x = 0; x < 4; x++) {
        oneFragment = new MianFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", "传递到的数据" + 1);
        oneFragment.setArguments(bundle);
        mian1Fragment = new Mian1Fragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("data", "传递到的数据" + 2);
        mian1Fragment.setArguments(bundle1);
        mian2Fragment = new Mian2Fragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("data", "传递到的数据" + 3);
        mian2Fragment.setArguments(bundle2);
        mian3Fragment = new Mian3Fragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("data", "传递到的数据" + 4);
        mian3Fragment.setArguments(bundle3);
        fragmentList.add(oneFragment);
        fragmentList.add(mian1Fragment);
        fragmentList.add(mian2Fragment);
        fragmentList.add(mian3Fragment);
//        }
        vpMain.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
        requestEachCombined(new PermissionsBase() {
            @Override
            public void isOK() {
                ToastUtil.showShortToast(mContext, "权限申请成功");
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE);

//
        initNotification();

//接口使用事例



    }

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    Notification notification;
    int id = 1;

    //初始化通知
    private void initNotification() {
        notificationManager = (NotificationManager) MyApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        String name = "下载";
        notificationManager = (NotificationManager) MyApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(MyApplication.getInstance(), "chat");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id + "", name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
        }
        builder.setContentTitle("正在更新...")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), R.mipmap.ic_launcher_round))
                .setDefaults(Notification.DEFAULT_LIGHTS) //设置通知的提醒方式： 呼吸灯
                .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知的优先级：最大
                .setAutoCancel(false)//设置通知被点击一次是否自动取消
                .setContentText("下载进度:" + "0%")
                .setProgress(100, 0, false)
                .setOngoing(true)
                .setChannelId(id + "");//无效

        notification = builder.build();
    }


    //接口定义

    //当前选中的项
    int currenttab = -1;

    @Override
    public void initData() {
        //        设置回调
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("checkCode", 111 + "");
        dataMap.put("loginName", "ceshi_han");
        dataMap.put("password ", "123456");
        if (presenterBase != null) {
            showProgressDialog();
            presenterBase.getList(dataMap);
        }
    }

    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

        public MyFrageStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        /**
         * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
         */
        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);//这句话要放在最前面，否则会报错
            //获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
            int currentItem = vpMain.getCurrentItem();
            if (currentItem == currenttab) {
                return;
            }
            currenttab = vpMain.getCurrentItem();
        }

    }

    @Override
    public boolean haveEventBus() {
        return true;
    }

    int download = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPostErro(DownloadEvent downloadEvent) {
        if (downloadEvent != null) {
            switch (downloadEvent.getOriginClass()) {

                case SdownloadStart:
                    Logger.e("MainActivity", "开始下载");
//                    RemoteViews     views = new RemoteViews(getPackageName(), R.layout.update_service);
                    builder.setProgress(100, download, false);
                    builder.setContentText("下载进度:" + download + "%");
                    notification = builder.build();
                    notificationManager.notify(id, notification);
                    //语句
                    break; //可选
                case downloadUpdate:
                    if (download < downloadEvent.getDownload()) {
                        download = downloadEvent.getDownload();
                        builder.setProgress(100, downloadEvent.getDownload(), false);
                        builder.setContentText("下载进度:" + downloadEvent.getDownload() + "%");
//                    builder.
                        notification = builder.build();
                        notificationManager.notify(id, notification);
                    }
                    //语句
                    break; //可选
                case downloadEnd:
                    Logger.e("MainActivity", "下载失败");
                    download = 0;
                    notificationManager.cancel(id);//取消通知
                    //语句
                    break; //可选
                case downloadSucceed:
                    Logger.e("MainActivity", "下载完成");
                    download = 0;
                    File file = downloadEvent.getFile();
                    builder.setContentTitle("下载完成")
                            .setContentText("点击安装")
                            .setAutoCancel(true);//设置通知被点击一次是否自动取消
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
                    PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);
                    notification = builder.setContentIntent(pi).build();
                    notificationManager.notify(id, notification);
                    break; //可选
                //你可以有任意数量的case语句
                default: //可选
                    //语句
                    break; //可选
            }
        }
    }

    //成功回调
    @Override
    public <T> void response(T response, int responseFlag) {
        dismissProgressDialog();
        ToastUtil.showShortToast(this, "成功");

    }

//    //关闭服务 停止下载
//    public void stopFinishSer(){
//        //也就是取消订阅
//        if (!DownloadService.compositeDisposable.isDisposed()) {
//            if (DownloadService.compositeDisposable.size() != 0) {
//                DownloadService.compositeDisposable.clear();
//            }
//        }
//        stopService(intent);
//    }

}
