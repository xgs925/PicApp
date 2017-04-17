package com.xcommon.utils.common;

import android.content.Context;
import android.widget.Toast;

import com.xcommon.AppContext;


/**
 * Created by Photostsrs on 2016/12/23.
 */

public class ToastUtil {
    public static void show(String msg){
        Toast.makeText(AppContext.mApplicationContext,msg, Toast.LENGTH_SHORT).show();
    }
    public static void show(int msg){
        show(msg+"");
    }
    public static void show(long msg){
        show(msg+"");
    }

    public static void show(Context context, String msg){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }
    public static void show(Context context, int msg){
        show(context,msg+"");
    }
    public static void show(Context context, long msg){
        show(context,msg+"");
    }
}
