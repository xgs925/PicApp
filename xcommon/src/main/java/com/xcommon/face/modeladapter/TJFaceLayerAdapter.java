package com.xcommon.face.modeladapter;

import com.xcommon.face.model.TJFaceLayer;

/**
 * Created by Photostsrs on 2016/11/18.
 */
public class TJFaceLayerAdapter extends TJMaterialLayerAdapter {
    public TJFaceLayerAdapter(TJFaceLayer tjFaceLayer, float scale) {
        super(tjFaceLayer, scale);
    }

    public TJFaceLayerAdapter() {
        super(new TJFaceLayer(), 1);
    }

    public TJFaceLayer getTjFaceLayer() {
        return (TJFaceLayer) getLayer();
    }

    public int getLeftEyeCenterX() {
        return (int) (Float.parseFloat(getTjFaceLayer().getLeftEyeCenterX()) * scale);
    }

    public void setLeftEyeCenterX(int leftEyeCenterX) {
        getTjFaceLayer().setLeftEyeCenterX(leftEyeCenterX + "");
    }

    public int getLeftEyeCenterY() {
        return (int) (Float.parseFloat(getTjFaceLayer().getLeftEyeCenterY()) * scale);
    }

    public void setLeftEyeCenterY(int leftEyeCenterY) {
        getTjFaceLayer().setLeftEyeCenterY(leftEyeCenterY + "");
    }

    public int getRightEyeCenterX() {
        return (int) (Float.parseFloat(getTjFaceLayer().getRightEyeCenterX()) * scale);
    }

    public void setRightEyeCenterX(int rightEyeCenterX) {
        getTjFaceLayer().setRightEyeCenterX(rightEyeCenterX + "");
    }

    public int getRightEyeCenterY() {
        return (int) (Float.parseFloat(getTjFaceLayer().getRightEyeCenterY()) * scale);
    }

    public void setRightEyeCenterY(int rightEyeCenterY) {
        getTjFaceLayer().setRightEyeCenterY(rightEyeCenterY + "");
    }

    public int getFaceWidth() {
        return (int) (Float.parseFloat(getTjFaceLayer().getFaceWidth()) * scale);
    }

    public void setFaceWidth(int faceWidth) {
        getTjFaceLayer().setFaceWidth(faceWidth + "");
    }

    public int getFaceHeight() {
        return (int) (Float.parseFloat(getTjFaceLayer().getFaceHeight()) * scale);
    }

    public void setFaceHeight(int faceHeight) {
        getTjFaceLayer().setFaceHeight(faceHeight + "");
    }
}
