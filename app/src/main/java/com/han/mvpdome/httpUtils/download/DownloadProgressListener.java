package com.han.mvpdome.httpUtils.download;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}