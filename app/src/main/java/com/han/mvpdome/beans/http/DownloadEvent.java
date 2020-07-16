package com.han.mvpdome.beans.http;

import java.io.File;
import java.io.Serializable;

//下载返回实体类
public class DownloadEvent implements Serializable {
    private ColorEnum originClass;
    private int download;
    private File file;

    public ColorEnum getOriginClass() {
        return originClass;
    }

    public void setOriginClass(ColorEnum originClass) {
        this.originClass = originClass;
    }


    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public enum ColorEnum {
        //        开始/下载进度  / 报错/结束
        SdownloadStart, downloadUpdate, downloadEnd, downloadSucceed
    }
}
