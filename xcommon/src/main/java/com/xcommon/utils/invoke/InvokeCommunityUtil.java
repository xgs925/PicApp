package com.xcommon.utils.invoke;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

/**
 * Created by Photostsrs on 2017/4/7.
 */

public class InvokeCommunityUtil {
    public static boolean requestVip(final Context context, final boolean showAlertDialog) {
        if (requestUserId(context) == null) return false;

        SharedPreferences sp = context.getSharedPreferences("tj.userinfo", Activity.MODE_PRIVATE);
        String vip = sp.getString("VIP", null);
        String vipDedline = sp.getString("vipDedline", null);
        if (!TextUtils.isEmpty(vip)
                && !TextUtils.isEmpty(vipDedline)
                && Long.parseLong(vipDedline) > System.currentTimeMillis() / 1000) {
            return true;
        }
        if (showAlertDialog){
            new AlertDialog.Builder(context).setMessage("此功能为会员专享，是否开通会员？").setNegativeButton("取消", null).setPositiveButton("去看看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(context, ActivityClassUtil.getPayActivity()));
                }
            }).show();
        }
        return false;
    }

    public static String requestUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("tj.userinfo", Activity.MODE_PRIVATE);
        String userID = sp.getString("userID", null);
        if (TextUtils.isEmpty(userID))
            context.startActivity(new Intent(context, ActivityClassUtil.getLoginActivity()));
        return userID;
    }

    public static boolean isUserIdNull(Context context) {
        SharedPreferences sp = context.getSharedPreferences("tj.userinfo", Activity.MODE_PRIVATE);
        String userID = sp.getString("userID", null);
        return TextUtils.isEmpty(userID);
    }
}
