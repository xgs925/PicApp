package com.xcommon.face.model;

/**
 * Created by Photostsrs on 2016/11/17.
 */
public class TJMaterialLayer extends TJLayer {
    private String centerX;
    private String centerY;
    private String referenceObject;
    private String angle;
    private String lock;

    public String getCenterX() {
        return centerX;
    }

    public void setCenterX(String centerX) {
        this.centerX = centerX;
    }

    public String getCenterY() {
        return centerY;
    }

    public void setCenterY(String centerY) {
        this.centerY = centerY;
    }

    public String getReferenceObject() {
        return referenceObject;
    }

    public void setReferenceObject(String referenceObject) {
        this.referenceObject = referenceObject;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }
}
