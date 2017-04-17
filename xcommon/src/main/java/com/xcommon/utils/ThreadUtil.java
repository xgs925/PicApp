package com.xcommon.utils;

import android.os.Handler;

/**
 * Created by Photostsrs on 2016/12/23.
 */

public class ThreadUtil {
    public static void post(Runnable runnable){
        Handler handler=new Handler();
        handler.post(runnable);
    }
    public static void post(long m, Runnable runnable){
        Handler handler=new Handler();
        handler.postDelayed(runnable,m);
    }

    public static void newThread(Runnable runnable){
        new Thread(runnable).start();
    }
}
