package com.xcommon.face;

import android.content.Context;

import com.xcommon.face.model.TJLayer;

import java.util.List;

/**
 * Created by Photostsrs on 2016/11/17.
 */
public interface IFaceOperation {
    void getModuleList(Context context, IFaceModuleListListener listener);

    List<TJLayer> readXml(String xmlName);

    void writeXml(List<TJLayer> tjLayers);

    void downloadModule(Context context, String xmlName, IFaceModuleDownloadListener listener);
}
