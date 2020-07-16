package com.han.mvpdome.httpUtils.download;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.os.FileObserver;
import android.util.Log;

import com.han.mvpdome.AppConstant;
import com.han.mvpdome.beans.http.DownloadEvent;
import com.han.mvpdome.utils.DeviceUtil;
import com.han.mvpdome.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;

import io.reactivex.disposables.CompositeDisposable;
import rx.Subscriber;

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";
    private String apkUrl = "";
    //retrofit订阅事件的监听
    public static CompositeDisposable compositeDisposable = new CompositeDisposable();
        private HashMap<String, FileObserver> hashMap;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        apkUrl = intent.getStringExtra("url");
        hashMap = new HashMap<>();
        download();
    }

    private void download() {
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Download download = new Download();
                download.setTotalFileSize(contentLength);
                download.setCurrentFileSize(bytesRead);
                int progress = (int) ((bytesRead * 100) / contentLength);
                download.setProgress(progress);
//下载中
                sendIntent(download);
            }
        };
        final File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), AppConstant.App.AppName + DeviceUtil.getVersionName(this) + ".apk");
        final String baseUrl = StringUtils.getHostName(apkUrl);
//
        DownloadEvent downloadEvent = new DownloadEvent();
        downloadEvent.setDownload(0);
        downloadEvent.setOriginClass(DownloadEvent.ColorEnum.SdownloadStart);
        EventBus.getDefault().post(downloadEvent);
        new DownloadAPI(baseUrl, listener).downloadAPK(apkUrl, outputFile, new Subscriber() {
            @Override
            public void onCompleted() {
//                完成
//                下载完成
                DownloadEvent downloadEvent = new DownloadEvent();
                downloadEvent.setDownload(0);
                downloadEvent.setOriginClass(DownloadEvent.ColorEnum.downloadSucceed);
                downloadEvent.setFile(outputFile);
//                hashMap.remove(baseUrl);
                EventBus.getDefault().post(downloadEvent);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                DownloadEvent downloadEvent = new DownloadEvent();
                downloadEvent.setDownload(0);
                downloadEvent.setOriginClass(DownloadEvent.ColorEnum.downloadEnd);
//                hashMap.remove(baseUrl);
                EventBus.getDefault().post(downloadEvent);

                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
            }
        });
//        hashMap.put(baseUrl, subscriber);
    }
    private void download1() {
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Download download = new Download();
                download.setTotalFileSize(contentLength);
                download.setCurrentFileSize(bytesRead);
                int progress = (int) ((bytesRead * 100) / contentLength);
                download.setProgress(progress);
//下载中
                sendIntent(download);
            }
        };
        final File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), AppConstant.App.AppName + DeviceUtil.getVersionName(this) + ".apk");
        final String baseUrl = StringUtils.getHostName(apkUrl);
//
        DownloadEvent downloadEvent = new DownloadEvent();
        downloadEvent.setDownload(0);
        downloadEvent.setOriginClass(DownloadEvent.ColorEnum.SdownloadStart);
        EventBus.getDefault().post(downloadEvent);
        new DownloadAPI(baseUrl, listener).downloadAPK(apkUrl, outputFile, new Subscriber() {
            @Override
            public void onCompleted() {
//                完成
//                下载完成
                DownloadEvent downloadEvent = new DownloadEvent();
                downloadEvent.setDownload(0);
                downloadEvent.setOriginClass(DownloadEvent.ColorEnum.downloadSucceed);
                downloadEvent.setFile(outputFile);
//                hashMap.remove(baseUrl);
                EventBus.getDefault().post(downloadEvent);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                DownloadEvent downloadEvent = new DownloadEvent();
                downloadEvent.setDownload(0);
                downloadEvent.setOriginClass(DownloadEvent.ColorEnum.downloadEnd);
//                hashMap.remove(baseUrl);
                EventBus.getDefault().post(downloadEvent);

                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
            }
        });
//        hashMap.put(baseUrl, subscriber);
    }

    private void sendIntent(Download download) {

        Log.e(TAG, "sendIntent: " + StringUtils.getDataSize(download.getCurrentFileSize())
                + "/" +
                StringUtils.getDataSize(download.getTotalFileSize()));

        DownloadEvent downloadEvent = new DownloadEvent();
        downloadEvent.setDownload(download.getProgress());
        downloadEvent.setOriginClass(DownloadEvent.ColorEnum.downloadUpdate);
        EventBus.getDefault().post(downloadEvent);
    }


}