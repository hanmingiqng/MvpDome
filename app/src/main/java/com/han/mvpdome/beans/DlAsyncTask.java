package com.han.mvpdome.beans;

import android.os.AsyncTask;
import android.util.Log;

public class DlAsyncTask extends AsyncTask<String, String, Long> {

    //耗时操作前
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //    执行耗时操作 进入子线程
    @Override
    protected Long doInBackground(String... urls) {
        int count = urls.length;
        long totalSize = 0;
        for (int i = 0; i < 100; i++) {
            totalSize = i;
//            totalSize+= Downloader
//            更新任务进度 改变值
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(i + "");
//            是否调用cancel()方法
            if (isCancelled()) {
                break;
            }
        }
        return totalSize;
    }

    //任务进度 值改变事件 在主线程执行
    @Override
    protected void onProgressUpdate(String... values) {
        Log.e("han", "onProgressUpdate: " + values[0]);

    }

    //程序结束
    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
        Log.e("han", "onPostExecute: " + aLong);
    }

    @Override
    protected void onCancelled() {
        Log.e("han", "onCancelled: 终止");
        super.onCancelled();

    }

    @Override
    protected void onCancelled(Long aLong) {
        Log.e("han", "onCancelled:" + aLong + " 终止");
        super.onCancelled(aLong);
    }
}
