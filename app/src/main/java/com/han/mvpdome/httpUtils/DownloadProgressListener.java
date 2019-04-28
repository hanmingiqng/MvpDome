package com.han.mvpdome.httpUtils;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}