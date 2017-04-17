package com.xcommon.view.layer;

import android.content.Context;


/**
 * Created by Photostsrs on 2016/11/25.
 */
public class ImageLayerView extends LayerView {
    public ImageLayerView(Context context) {
        super(context);
    }

    @Override
    protected float ajustScale(float scale) {
        if(getWidth()*scale>4*layerLayoutW) return 4f*layerLayoutW/getWidth();
        if(getHeight()*scale>4*layerLayoutH) return 4f*layerLayoutH/getHeight();
        return super.ajustScale(scale);
    }
}
