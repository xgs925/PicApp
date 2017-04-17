package com.xcommon;

import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by Photostsrs on 2017/3/21.
 */

public class AppContext {
    public static Context mApplicationContext;
    public static boolean isDebug;

    public static void init(Context context, boolean debug) {
        mApplicationContext = context.getApplicationContext();
        isDebug = debug;
        FileDownloader.init(context);
    }
}
