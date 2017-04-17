package com.xcommon.face.modeladapter;

import com.xcommon.face.model.TJBackgroundLayer;

/**
 * Created by Photostsrs on 2016/11/18.
 */
public class TJBackgroundLayerAdapter extends TJLayerAdapter {
    public TJBackgroundLayerAdapter(TJBackgroundLayer tjBackgroundLayer, float scale) {
        super(tjBackgroundLayer,scale);
    }

    public TJBackgroundLayerAdapter() {
        super(new TJBackgroundLayer(),1);
    }

    public TJBackgroundLayer getTJBackgroundLayer() {
        return (TJBackgroundLayer) getLayer();
    }
}
