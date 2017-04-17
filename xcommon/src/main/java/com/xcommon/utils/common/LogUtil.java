package com.xcommon.utils.common;

import android.util.Log;

import com.xcommon.AppContext;

/**
 * Created by Photostsrs on 2016/12/15.
 */

public class LogUtil {
    public static boolean isDebug = AppContext.isDebug;
    private static final String TAG = "gus";

    public static void i(String msg)
    {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg)
    {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg)
    {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg)
    {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }
}
