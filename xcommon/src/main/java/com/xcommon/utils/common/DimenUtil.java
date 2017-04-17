package com.xcommon.utils.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import com.xcommon.AppContext;

import java.lang.reflect.Field;

/**
 * Created by Photostsrs on 2016/12/30.
 */

public class DimenUtil {
    public static int getWindowWidth(Activity context) {
        return context.getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int getWindowHeight(Activity context) {
        return context.getWindowManager().getDefaultDisplay().getHeight();
    }
    /**
     * 获取状态栏/通知栏的高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 获取屏幕的宽高
     */
    public static void measure(Context context) {
        Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //不含虚拟按键
        windowManager.getDefaultDisplay().getSize(point);
        //包含虚拟按键
        //windowManager.getDefaultDisplay().getRealSize(point);
        //屏幕宽度
        int height = point.y;
        //屏幕高度
        int width = point.x;
    }

    /**
     * 根据手机的分辨率从 dp 单位 转成为 px(像素)
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int dip2px(float dipValue) {
        final float scale = AppContext.mApplicationContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
