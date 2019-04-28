package com.han.mvpdome.httpUtils;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.han.mvpdome.beans.DownloadEvent;
import com.han.mvpdome.utils.DeviceUtil;
import com.han.mvpdome.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import rx.Subscriber;

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";
    private String apkUrl = "";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        apkUrl = intent.getStringExtra("url");
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

                sendIntent(download);
            }
        };
        File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), "didaxiaozhen" + DeviceUtil.getVersionName(this) + ".apk");
        String baseUrl = StringUtils.getHostName(apkUrl);
//
        DownloadEvent downloadEvent = new DownloadEvent();
        downloadEvent.setDownload(0);
        downloadEvent.setOriginClass("downloadStart");
        EventBus.getDefault().post(downloadEvent);
        new DownloadAPI(baseUrl, listener).downloadAPK(apkUrl, outputFile, new Subscriber() {
            @Override
            public void onCompleted() {
//                完成
                DownloadEvent downloadEvent = new DownloadEvent();
                downloadEvent.setDownload(0);
                downloadEvent.setOriginClass("downloadSucceed");
                EventBus.getDefault().post(downloadEvent);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                DownloadEvent downloadEvent = new DownloadEvent();
                downloadEvent.setDownload(0);
                downloadEvent.setOriginClass("downloadEnd");
                EventBus.getDefault().post(downloadEvent);
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
            }
        });
    }

    private void sendIntent(Download download) {

        Log.e(TAG, "sendIntent: " + StringUtils.getDataSize(download.getCurrentFileSize())
                + "/" +
                StringUtils.getDataSize(download.getTotalFileSize()));

        DownloadEvent downloadEvent = new DownloadEvent();
        downloadEvent.setDownload(download.getProgress());
        downloadEvent.setOriginClass("downloadUpdate");
        EventBus.getDefault().post(downloadEvent);
    }


}