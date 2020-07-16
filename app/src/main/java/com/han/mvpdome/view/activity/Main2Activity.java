package com.han.mvpdome.view.activity;

import android.util.Log;

import com.han.mvpdome.R;
import com.han.mvpdome.Text;
import com.han.mvpdome.base.BaseActivity;
import com.han.mvpdome.presenter.impl.MainAPresenterImpl;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.utils.ToastUtil;
import com.han.mvpdome.view.inter.IBaseView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.OnClick;

public class Main2Activity extends BaseActivity  {

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
        todata();
    }

    private void todata() {
        Logger.d("han", "onCreate: " + getClass().getSimpleName());//打印当前活动名
//        Ceshi ceshi = new Ceshi("sss", 1);
//        Intent intent = new Intent(mContext, MainActivity.class);
//        Binder b = new Binder();
//        intent.putExtra("1", ceshi);
//        startActivity(intent);
//使用AsyncTask
//        final DlAsyncTask dlAsyncTask = new DlAsyncTask();
//        dlAsyncTask.execute("111");
//        线程池
        //创建基本线程池
//        CorePoolSize 线程的核心线程数。 maximumPoolSize 线程池所能容纳的最大线程数
//        keepAliveTime 非核心线程闲置时的超时时长 unit 用于指定keepAliveTime
// TimeUnit.NANOSECONDS  纳秒
//TimeUnit.MICROSECONDS 微秒
//TimeUnit.MILLISECONDS 毫秒
//TimeUnit.SECONDS    秒
//TimeUnit.MINUTES    分钟
//TimeUnit.HOURS      小时
//TimeUnit.DAYS       天
//        workQueue 线程池中的任务队列，通过线程池execute方法提交的Runnable对象会存储在这个参数中。
//        基本线程池
        final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,
                5, 1, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(20));
//        只有核心线程 一种线程数固定的线程池
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
//        没有核心线程，非核心线程是无界的
        ExecutorService pool2 = Executors.newCachedThreadPool();
//l是一个能实现定时和周期性任务的线程池。
        ScheduledExecutorService pool3 = Executors.newScheduledThreadPool(4);
//单个工作线程的线程池。
        ExecutorService pool4 = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 30; i++) {
            final int finali = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        Log.e("TAG", "run : " + finali + "  当前线程：" + Thread.currentThread().getName());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            try {
//                获取最大队列
                threadPoolExecutor.getCorePoolSize();
                Future future = threadPoolExecutor.submit(runnable);
            } catch (RejectedExecutionException e) {
                Log.e("TAG", "run :  队列已满");
//                e.printStackTrace();
            }

//            future.cancel(true);
//            fixedThreadPool.execute(runnable);
        }
//threadFactory
//线程工厂
//        ThreadFactory factory =new ThreadFactory() {
//            //线程安全的Integer操作类
//            private final AtomicInteger mCount =new AtomicInteger(1);
//
//            @Override
//            public Thread newThread(Runnable r) {
//                return new Thread(r, "new Thread #" + mCount.getAndIncrement());
//            }
//

//        final Handler handler=new Handler();
//        Runnable runnable=new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                //要做的事情
//                    dlAsyncTask.cancel(false);
////                    dlAsyncTask.onc
//            }
//        };
//
//        handler.postDelayed(runnable, 2000);


    }

    @Override
    public boolean haveEventBus() {
        return false;
    }


    @Override
    public<T>  void response(T response, int responseFlag) {

    }
}
