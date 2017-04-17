package com.xcommon.tjbitmap;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Photostsrs on 2016/7/26.
 */
public class Info implements Parcelable {
    public static final String EXTRA_INFO = "info";
    private String title;
    private int type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Info(String title, int type) {
        this.title = title;
        this.type = type;
    }

    protected Info(Parcel in) {
        title = in.readString();
        type = in.readInt();
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(type);
    }
}
