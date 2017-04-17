package com.xcommon.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.xcommon.AppContext;
import com.xcommon.Constant;
import com.xcommon.utils.common.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.functions.Action1;

/**
 * Created by Photostsrs on 2017/1/12.
 */

public class ExternalStorageUtil {
    public static final String GIF = ".gif";
    public static final String JPG = ".jpg";
    public static final String PNG = ".png";
    private static String saveDir = Environment.getExternalStorageDirectory() + Constant.ROOT_FOLDER + "/";

    public static boolean checkPermission(Activity activity) {
        final boolean[] permission = {false};
        final String slert="no permission";
        RxPermissions permissions = new RxPermissions(activity);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    permission[0] = true;
                } else {
                    permission[0] = false;
                    ToastUtil.show(slert);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        return permission[0];
    }

    private static boolean mkDirIfNotExist() {
        boolean isMkDir;
        File file = new File(saveDir);
        if (!file.exists()) {
            isMkDir = file.mkdir();
        } else {
            isMkDir = true;
        }
        return isMkDir;
    }

    private static String getSaveName() {
        return "tj" + System.currentTimeMillis();
    }

    private static void sendBroadcast(File file) {
        AppContext.mApplicationContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    public static boolean saveGif(ByteArrayOutputStream baos) {
        return saveGif(baos,getSaveName()+GIF);
    }
    public static boolean saveGif(ByteArrayOutputStream baos, String name) {
        if (!mkDirIfNotExist()) return false;
        String path = saveDir + name;
        File file = new File(path);
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            FileOutputStream fos = new FileOutputStream(path);
            baos.writeTo(fos);
            baos.flush();
            fos.flush();
            baos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        sendBroadcast(file);
        return true;
    }

    public static boolean saveGif2Cache(ByteArrayOutputStream baos, String name) {
        String path = AppContext.mApplicationContext.getExternalCacheDir().getAbsolutePath() + "/" + name;
        File file = new File(path);
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            FileOutputStream fos = new FileOutputStream(path);
            baos.writeTo(fos);
            baos.flush();
            fos.flush();
            baos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static String getSaveFileDir(){
        return AppContext.mApplicationContext.getExternalFilesDir(null).getAbsolutePath()+ "/";
    }
    public static boolean saveGif2File(ByteArrayOutputStream baos, String name) {
        String path = getSaveFileDir() + name;
        File file = new File(path);
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            FileOutputStream fos = new FileOutputStream(path);
            baos.writeTo(fos);
            baos.flush();
            fos.flush();
            baos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean saveBitmap2Cache(Bitmap bitmap, String name, String type) {
        String path = AppContext.mApplicationContext.getExternalCacheDir().getAbsolutePath() + "/" + name;
        File file = new File(path);
        FileOutputStream fOut = null;
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            if (JPG.equals(type)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            } else {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            }
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveJpgBitmap(Activity activity,Bitmap bitmap) {
        if(!checkPermission(activity)) return false;
        if (bitmap == null) return false;
        if (!mkDirIfNotExist()) return false;
        String path = saveDir + getSaveName() + JPG;
        File file = new File(path);
        FileOutputStream fOut = null;
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        sendBroadcast(file);
        return true;
    }

    public static boolean savePngBitmap(Activity activity,Bitmap bitmap) {
        if(!checkPermission(activity)) return false;
        if (bitmap == null) return false;
        if (!mkDirIfNotExist()) return false;
        String path = saveDir + getSaveName() + PNG;
        File file = new File(path);
        FileOutputStream fOut = null;
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        sendBroadcast(file);
        return true;
    }

    public static void deleteFileInCache(String fileName){
        File file=new File(AppContext.mApplicationContext.getExternalCacheDir()+ File.separator+fileName);
        if(!file.exists())return;
        file.delete();
    }
}
