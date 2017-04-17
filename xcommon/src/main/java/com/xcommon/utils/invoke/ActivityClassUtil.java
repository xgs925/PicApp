package com.xcommon.utils.invoke;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Photostsrs on 2016/8/9.
 */
public class ActivityClassUtil {
    public static Class getAlbumMainActivity() {
        try {
            return Class.forName("com.imsiper.tool.module.album.activity.AlbumMainActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Class getReleaseThemeActivity() {
        try {
            return Class.forName("com.imsiper.community.tjpayutils.PayActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getPayActivity() {
        try {
            return Class.forName("com.imsiper.community.tjpayutils.PayActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getLoginActivity() {
        try {
            return Class.forName("com.imsiper.community.TJUtils.LoginActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getShareActivity() {
        try {
            return Class.forName("com.imsiper.community.main.Ui.ShareActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Class getMatActivity() {
        try {
            return Class.forName("com.photostars.xmat.activity.MatActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getMaskActivity() {
        try {
            return Class.forName("com.photostars.xmask.activity.NewMaskActivity");
//            return Class.forName("com.photostars.xmask.activity.MaskActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getBoxBlendActivity() {
        try {
            return Class.forName("com.photostars.xblend.activity.BoxBlendActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRunningActivityName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo cinfo = runningTasks.get(0);
        ComponentName component = cinfo.topActivity;
        String name = component.getClassName();
        String activityName = name.substring(name.lastIndexOf(".") + 1);
        Log.d("ActivityClassUtil", "getRunningActivityName: " + activityName);
        return activityName;
    }

    public static Class getCollectCardActivity() {
        try {
            return Class.forName("com.imsiper.tjminepage.Ui.MyCollectActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getMyCollectionActivity() {
        try {
            return Class.forName("com.imsiper.tool.module.material.activity.MyCollectionActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getCropActivity() {
        try {
            return Class.forName("com.photostars.xcrop.NewCropActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getNewCropActivity() {
        try {
            return Class.forName("com.photostars.xcrop.NewCropActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Class getLayerActivity() {
        try {
            return Class.forName("com.imsiper.tool.module.layer.activity.LayerActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getAddSingleActivity() {
        try {
            return Class.forName("com.photostars.xalbum.Activity.AddSingleActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getCropPartActivity() {
        try {
            return Class.forName("com.photostars.xcrop.CropPartActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getCommunityMainActivity() {
        try {
            return Class.forName("com.imsiper.community.main.Ui.MainActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getCollectMTActivity() {
        try {
            return Class.forName("com.imsiper.tool.module.material.activity.CollectMTActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getEventActivity() {
        try {
            return Class.forName("com.imsiper.community.main.Ui.EventActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getOperationXML() {
        try {
            return Class.forName("com.imsiper.community.tjxmlutil.OperationXML");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getToolMainActivity() {
        try {
            return Class.forName("com.imsiper.tool.ToolMainActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
