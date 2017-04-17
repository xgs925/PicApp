package com.xcommon.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by Photostsrs on 2016/5/19.
 */
public class ImageLoaderUtil {
    static final DisplayImageOptions memOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new SimpleBitmapDisplayer()).build();
    static final DisplayImageOptions discOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new SimpleBitmapDisplayer()).build();

    public static DisplayImageOptions getMemOptions() {
        return memOptions;
    }

    public static DisplayImageOptions getDiscOptions() {
        return memOptions;
    }

    public static ImageSize getGridDefaultImageSize() {
        ImageSize imageSize = new ImageSize(100, 100);
        return imageSize;
    }
}
