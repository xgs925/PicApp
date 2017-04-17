package com.xcommon.face.model;

/**
 * Created by Photostsrs on 2016/11/17.
 */
public class TJLayer {
    private String width;
    private String height;
    private String title;
    private String imageType;
    private String flip;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getFlip() {
        return flip;
    }

    public void setFlip(String flip) {
        this.flip = flip;
    }

    @Override
    public String toString() {
        return "TJLayer{" +
                "width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", title='" + title + '\'' +
                ", imageType='" + imageType + '\'' +
                ", flip='" + flip + '\'' +
                '}';
    }
}
