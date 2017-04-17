package com.xcommon.face.modeladapter;

import com.xcommon.face.model.TJLayer;

/**
 * Created by Photostsrs on 2016/11/18.
 */
public class TJLayerAdapter {
    float scale=1;
    private TJLayer tjLayer;

    public TJLayerAdapter(TJLayer tjLayer, float scale) {
        this.tjLayer = tjLayer;
        this.scale=scale;
    }
    public TJLayer getLayer() {
        return tjLayer;
    }

    public int getWidth() {
        return (int) (Float.parseFloat(tjLayer.getWidth())*scale);
    }

    public void setWidth(int width) {
        tjLayer.setWidth(width + "");
    }

    public int getHeight() {
        return (int) (Float.parseFloat(tjLayer.getHeight())*scale);
    }

    public void setHeight(int height) {
        tjLayer.setHeight(height + "");
    }

    public String getTitle() {
        return tjLayer.getTitle();
    }

    public void setTitle(String title) {
        tjLayer.setTitle(title);
    }

    public int getImageType() {
        return Integer.parseInt(tjLayer.getImageType());
    }

    public void setImageType(int imageType) {
        tjLayer.setImageType(imageType + "");
    }

    public boolean getFlip() {
        return "0".equals(tjLayer.getFlip()) ? false : true;
    }

    public void setFlip(boolean flip) {
        if(flip){
            tjLayer.setFlip("1");
        }else {
            tjLayer.setFlip("0");
        }
    }
}
