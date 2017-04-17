package com.xcommon.utils.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.notification.BaseNotificationItem;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationHelper;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationListener;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.xcommon.AppContext;
import com.xcommon.R;
import com.xcommon.network.Network;
import com.xcommon.utils.ExternalStorageUtil;
import com.xcommon.utils.NetworkUtil;
import com.xcommon.utils.ThreadUtil;
import com.xcommon.utils.common.AppUtil;
import com.xcommon.utils.common.LogUtil;
import com.xcommon.utils.common.SPUtils;
import com.xcommon.utils.common.ToastUtil;
import com.xcommon.utils.update.model.UpdateInfo;

import java.io.File;
import java.text.DecimalFormat;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Photostsrs on 2017/2/8.
 */

public class UpdateUtil {
    public void checkUpdate(final Activity context) {
        ExternalStorageUtil.deleteFileInCache("update.apk");
        if (!NetworkUtil.isWifi(AppContext.mApplicationContext)) return;
        String version = AppUtil.getVersionNum();
        String[] versionSplit = version.split("\\.");
        if ("".equals(version)) return;
        String versionNum;
        switch (versionSplit.length) {
            case 1:
                versionNum = versionSplit[0] + "00";
                break;
            case 2:
                versionNum = versionSplit[0] + versionSplit[1] + "0";
                break;
            default:
                versionNum = versionSplit[0] + versionSplit[1] + versionSplit[2];
                break;
        }
//        if(AppContext.isDebug)versionNum="200";
        Network.getUpdateApi()
                .getUpdateInfo(Network.AUTH_KEY, versionNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(final UpdateInfo updateInfo) {
                        boolean must=false;
                        if ("1".equals(updateInfo.status)) {
                            String ignore = (String) SPUtils.get(context, "ignoreVersion", "0");
                            if (ignore.equals(updateInfo.updateVersion)) return;
                        }else if("3".equals(updateInfo.status)){
                            must=true;
                        }else {
                            return;
                        }
                        String url = updateInfo.downUrl;
                        if (url == null) return;
                        final String finalUrl = url;
                        final boolean finalMust = must;
                        ThreadUtil.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_alert, null);
                                    final CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_ignore);

                                    AlertDialog.Builder builder=new AlertDialog.Builder(context).setView(view).setTitle("更新提示").setMessage("新版本已上线，是否更新?").setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!checkBox.isChecked()) return;
                                            SPUtils.put(context, "ignoreVersion", updateInfo.updateVersion);
                                        }
                                    }).setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            update(finalUrl);
                                        }
                                    });
                                    if(finalMust){
                                        View ignore = view.findViewById(R.id.v_ignore);
                                        ignore.setVisibility(View.GONE);
                                        builder.setCancelable(false);
                                        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                context.finish();
                                            }
                                        });
                                    }
                                    builder.show();
                                }catch (Exception e){

                                }
                            }
                        });
                    }
                });
    }

    private void update(String url) {
//        if (downloadId == 0) {
//            ToastUtil.show("后台下载中。。。");
//            downloadId = FileDownloader.getImpl().create(url)
//                    .setPath(AppContext.mApplicationContext.getExternalCacheDir().getAbsolutePath() + File.separator + "update.apk")
//                    .setListener(new NotificationListener(new FileDownloadNotificationHelper<NotificationItem>()))
//                    .start();
//        }
    }

    private void openApkFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        AppContext.mApplicationContext.startActivity(intent);
    }

    private int downloadId = 0;

//    private class NotificationListener extends FileDownloadNotificationListener {
//        public NotificationListener(FileDownloadNotificationHelper helper) {
//            super(helper);
//        }
//
//        @Override
//        protected BaseNotificationItem create(BaseDownloadTask task) {
//            return new NotificationItem(task.getId(), "正在下载。。。", "");
//        }
//
//        @Override
//        public void destroyNotification(BaseDownloadTask task) {
//            super.destroyNotification(task);
//            downloadId = 0;
//        }
//
//        @Override
//        protected void completed(BaseDownloadTask task) {
//            super.completed(task);
//            File file = new File(task.getPath());
//            openApkFile(file);
//        }
//    }

//    public static class NotificationItem extends BaseNotificationItem {
//        NotificationCompat.Builder builder;
//
//        private NotificationItem(int id, String title, String desc) {
//            super(id, title, desc);
//            builder = new NotificationCompat.Builder(FileDownloadHelper.getAppContext());
//            builder.setDefaults(Notification.DEFAULT_LIGHTS)
//                    .setTicker("正在下载")
//                    .setOngoing(true)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setContentTitle(getTitle())
//                    .setContentText(desc)
//                    .setSmallIcon(R.mipmap.ic_launcher);
//        }
//
//        @Override
//        public void show(boolean statusChanged, int status, boolean isShowProgress) {
//            DecimalFormat df = new DecimalFormat("#.##");
//            String desc = df.format(getSofar() / (1024f * 1024f)) + "M /" + df.format(getTotal() / (1024f * 1024f)) + "M";
//            builder.setContentTitle(getTitle())
//                    .setContentText(desc);
//            builder.setProgress(getTotal(), getSofar(), !isShowProgress);
//            getManager().notify(getId(), builder.build());
//        }
//    }
}
