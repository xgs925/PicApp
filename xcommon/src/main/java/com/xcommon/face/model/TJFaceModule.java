package com.xcommon.face.model;

/**
 * Created by Photostsrs on 2016/11/19.
 */
public class TJFaceModule {
    private String picUrl;
    private String xmlName;
    private boolean downloadStatus;

    public TJFaceModule(String picUrl, String xmlName, boolean downloadStatus) {
        this.downloadStatus = downloadStatus;
        this.picUrl = picUrl;
        this.xmlName = xmlName;
    }


    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public boolean isDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(boolean downloadStatus) {
        this.downloadStatus = downloadStatus;
    }
}
