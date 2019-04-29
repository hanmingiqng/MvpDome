package com.han.mvpdome.beans;

import java.io.Serializable;

//下载返回实体类
public class DownloadEvent implements Serializable {
    private ColorEnum originClass;
    private int download;

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

    public enum ColorEnum {
        //        开始/下载进度  / 报错/结束
        SdownloadStart, downloadUpdate, downloadEnd, downloadSucceed
    }
}
