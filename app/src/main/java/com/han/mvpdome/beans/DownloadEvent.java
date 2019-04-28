package com.han.mvpdome.beans;

import java.io.Serializable;

public class DownloadEvent implements Serializable {
    private String originClass;
    private int download;

    public String getOriginClass() {
        return originClass;
    }

    public void setOriginClass(String originClass) {
        this.originClass = originClass;
    }


    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }
}
