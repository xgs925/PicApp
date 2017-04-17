package com.xcommon.face.modeladapter;

import com.xcommon.face.model.TJMaterialLayer;

/**
 * Created by Photostsrs on 2016/11/18.
 */
public class TJMaterialLayerAdapter extends TJLayerAdapter {
    public TJMaterialLayerAdapter(TJMaterialLayer tjMaterialLayer, float scale) {
        super(tjMaterialLayer,scale);
    }

    public TJMaterialLayerAdapter() {
        super(new TJMaterialLayer(),1);
    }
    public TJMaterialLayer getTjMaterialLayer(){
        return (TJMaterialLayer) getLayer();
    }
    public int getCenterX() {
        return (int) (Float.parseFloat(getTjMaterialLayer().getCenterX())*scale);
    }

    public void setCenterX(int centerX) {
        getTjMaterialLayer().setCenterX(centerX+"");
    }

    public int getCenterY() {
        return (int) (Float.parseFloat(getTjMaterialLayer().getCenterY())*scale);
    }

    public void setCenterY(int centerY) {
        getTjMaterialLayer().setCenterY(centerY+"");
    }

    public int getReferenceObject() {
        return Integer.parseInt(getTjMaterialLayer().getReferenceObject());
    }

    public void setReferenceObject(int referenceObject) {
        getTjMaterialLayer().setReferenceObject(referenceObject+"");
    }

    public float getAngle() {
        return Float.parseFloat(getTjMaterialLayer().getAngle());
    }

    public void setAngle(float angle) {
        getTjMaterialLayer().setAngle(angle+"");
    }

    public float getRotation(){
        return (float) (getAngle() / Math.PI * 180);
    }

    public void setRotation(float rotation){
        setAngle((float) (rotation/ 360 * 2 * Math.PI));
    }

    public boolean getLock() {
        return "0".equals(getTjMaterialLayer().getFlip()) ? false : true;
    }

    public void setLock(boolean lock) {
        if(lock){
            getTjMaterialLayer().setLock(0+"");
        }else {
            getTjMaterialLayer().setLock(1+"");
        }
    }
}
