package com.xcommon.face;

/**
 * Created by Photostsrs on 2016/11/19.
 */
public interface IFaceModuleDownloadListener {
    void onSuccess();

    void onFail();

    void downloadProgressUpdate(int progress);
}
