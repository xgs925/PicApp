package com.xcommon.utils.toolfile;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.xcommon.AppContext;
import com.xcommon.tjbitmap.Info;
import com.xcommon.tjbitmap.TJBitmap;
import com.xcommon.utils.ThreadUtil;
import com.xcommon.utils.common.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Photostsrs on 2016/6/28.
 */
public class AppFileUtil {
    public static String TAG = "AppFileUtil";
    public static String FOLDER = "/app/";
    private static final String JPG = ".jpg";
    private static final String PNG = ".png";


    public static String getSystemTimeMillis() {
        return System.currentTimeMillis() + "";
    }

    public static String getDir() {
        return AppContext.mApplicationContext.getDir("tj", Context.MODE_PRIVATE) + FOLDER;
    }

    public static boolean isExist(String fileName) {
        File file = new File(getDir() + fileName);
        return file.exists();
    }

    //删除文件
    public static void deleteFile(final File file) {
        if (!file.exists()) return; // 判断文件是否存在
        if (file.isFile()) { // 判断是否是文件
            ToolBitmapCache.getInstance().deleteBitmap(file.getAbsolutePath());
            ThreadUtil.newThread(new Runnable() {
                @Override
                public void run() {
                    file.delete();
                }
            });
        } else if (file.isDirectory()) { // 否则如果它是一个目录
            File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
            for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
            }
        }
    }

    //创建文件夹
    public static void createFolderIfNotExist(Context context) {
        File file = new File(getDir());
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {

            }
        }
    }

    public static void createFolderIfNotExist() {
        File file = new File(getDir());
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {

            }
        }
    }

    public static void clear(List<String> keepFileNames) {
        File tool = new File(getDir());
        String[] toolFiles = tool.list();
        if (keepFileNames == null) {
            deleteFile(tool);
        } else {
            for (String toolFile : toolFiles) {
                if (!keepFileNames.contains(toolFile))
                    deleteFile(new File(getDir() + toolFile));
            }
        }
    }

    public static void clearTemp() {
        clear(null);
    }


    public static Bitmap decodeBitmapFile(String pathName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public static void saveBitmap(final Bitmap bitmap, String name) {
        if (bitmap == null) return;
        final String path = getDir() + name + ".jpg";
        ToolBitmapCache.getInstance().addBitmap(path, bitmap);
        ThreadUtil.newThread(new Runnable() {
            @Override
            public void run() {
                createFolderIfNotExist();
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void savePNGBitmap(final Bitmap bitmap, String name) {
        if (bitmap == null) return;
        final String path = getDir() + name + ".png";
        ToolBitmapCache.getInstance().addBitmap(path, bitmap);
        ThreadUtil.newThread(new Runnable() {
            @Override
            public void run() {
                createFolderIfNotExist();
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Bitmap getPNGBitmapByName(String name) {
        String path = getDir() + name + ".png";
        Bitmap bitmap = ToolBitmapCache.getInstance().getBitmap(path);
        if (bitmap != null) return bitmap;
        File file = new File(path);
        if (!file.exists()) {
            LogUtil.e(TAG, name + ".png 不存在！");
            return null;
        }
        return decodeBitmapFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmapByNameWithSuffix(String name) {
        String path = getDir() + name;
        Bitmap bitmap = ToolBitmapCache.getInstance().getBitmap(path);
        if (bitmap != null) return bitmap;
        File file = new File(path);
        if (!file.exists()) {
            LogUtil.e(TAG, name + "不存在！");
            return null;
        }
        return decodeBitmapFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmapByName(String name) {
        String path = getDir() + name + ".jpg";
        Bitmap bitmap = ToolBitmapCache.getInstance().getBitmap(path);
        if (bitmap != null) return bitmap;
        File file = new File(path);
        if (!file.exists()) {
            LogUtil.e(TAG, name + ".jpg 不存在！");
            return null;
        }
        bitmap = decodeBitmapFile(file.getAbsolutePath());
        ToolBitmapCache.getInstance().addBitmap(path, bitmap);
        return bitmap;
    }

    public static Bitmap getBitmapByTitleAndType(String title, int type) {
        return getBitmapByNameWithSuffix(getNameByTitleAndType(title, type));
    }

    public static String getNameByTitleAndType(String title, int type) {
        String name = null;
        switch (type) {
            case 0:
                name = "rgba" + title + JPG;
                break;
            case 1:
                name = "rgba" + title + PNG;
                break;
            case 2:
                name = title + PNG;
                break;
        }

        return name;
    }

    public static void deleteByInfo(Info info) {
        switch (info.getType()) {
            case 0:
                deleteJPGByName(getNameByTitleAndType(info.getTitle(), info.getType()));
                break;
            case 1:
                deleteJPGByName(getNameByTitleAndType(info.getTitle(), info.getType()));
                break;
            case 2:
                deletePNGByName(info.getTitle());
                deleteJPGByName("rgba" + info.getTitle());
                deleteJPGByName("mask" + info.getTitle());
                break;
        }
    }

    public static TJBitmap getTjBitmapByInfo(Info info) {
        Bitmap showBitmap = getBitmapByNameWithSuffix(getNameByTitleAndType(info.getTitle(), info.getType()));
        TJBitmap tjBitmap = null;
        switch (info.getType()) {
            case 0:
                tjBitmap = new TJBitmap(showBitmap, info);
                break;
            case 1:
                tjBitmap = new TJBitmap(showBitmap, info);
                break;
            case 2:
                Bitmap rgbBitmap = getBitmapByName("rgba" + info.getTitle());
                Bitmap maskBitmap = getBitmapByName("mask" + info.getTitle());
                tjBitmap = new TJBitmap(showBitmap, rgbBitmap, maskBitmap);
                break;
        }
        return tjBitmap;
    }

    public static void deleteJPGByName(String name) {
        deleteFile(new File(getDir() + name + ".jpg"));
    }

    public static void deletePNGByName(String name) {
        deleteFile(new File(getDir() + name + ".png"));
    }

    public static boolean renameFile(String orName, String newName) {
        String oldPath = getDir() + orName;
        File orFile = new File(oldPath);
        if (!orFile.exists()) return false;
        String newPath = getDir() + newName;
        File newFile = new File(newPath);
        orFile.renameTo(newFile);
        ToolBitmapCache.getInstance().renameBitmap(oldPath, newPath);
        return true;
    }

    public static boolean copyFile(String orName, String newName) {
        String oldPath = getDir() + orName;
        final File orFile = new File(oldPath);
        if (!orFile.exists()) return false;
        final String newPath = getDir() + newName;
        ToolBitmapCache.getInstance().copy(oldPath, newPath);
        ThreadUtil.newThread(new Runnable() {
            @Override
            public void run() {
                File newFile = new File(newPath);
                InputStream inStream = null; //读入原文件
                int byteread = 0;
                try {
                    inStream = new FileInputStream(orFile);
                    FileOutputStream fs = new FileOutputStream(newFile);
                    byte[] buffer = new byte[1024];
                    while ((byteread = inStream.read(buffer)) != -1) {
                        fs.write(buffer, 0, byteread);
                    }
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    public static Info copyByInfo(Info info) {
        Info newInfo = new Info(info.getTitle().replace(info.getTitle().substring(0, 13), getSystemTimeMillis()), info.getType());
        switch (info.getType()) {
            case 0:
                copyFile("rgba" + info.getTitle() + JPG, "rgba" + newInfo.getTitle() + JPG);
                break;
            case 1:
                copyFile("rgba" + info.getTitle() + PNG, "rgba" + newInfo.getTitle() + PNG);
                break;
            case 2:
                copyFile("rgba" + info.getTitle() + JPG, "rgba" + newInfo.getTitle() + JPG);
                copyFile("mask" + info.getTitle() + JPG, "mask" + newInfo.getTitle() + JPG);
                copyFile(info.getTitle() + PNG, newInfo.getTitle() + PNG);
                break;
        }
        return newInfo;
    }

    public static boolean copyFromTemp(String name) {
        String oldPath = ToolFileUtil.getToolDir() + name;
        final File orFile = new File(oldPath);
        if (!orFile.exists()) return false;
        final String newPath = getDir() + name;
        ToolBitmapCache.getInstance().copy(oldPath, newPath);
        ThreadUtil.newThread(new Runnable() {
            @Override
            public void run() {
                File newFile = new File(newPath);
                InputStream inStream = null; //读入原文件
                int byteread = 0;
                try {
                    inStream = new FileInputStream(orFile);
                    FileOutputStream fs = new FileOutputStream(newFile);
                    byte[] buffer = new byte[1024];
                    while ((byteread = inStream.read(buffer)) != -1) {
                        fs.write(buffer, 0, byteread);
                    }
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    public static boolean copy2Temp(String name) {
        String oldPath = getDir() + name;
        final File orFile = new File(oldPath);
        if (!orFile.exists()) return false;
        final String newPath = ToolFileUtil.getToolDir() + name;
        ThreadUtil.newThread(new Runnable() {
            @Override
            public void run() {
                File newFile = new File(newPath);
                InputStream inStream = null; //读入原文件
                int byteread = 0;
                try {
                    inStream = new FileInputStream(orFile);
                    FileOutputStream fs = new FileOutputStream(newFile);
                    byte[] buffer = new byte[1024];
                    while ((byteread = inStream.read(buffer)) != -1) {
                        fs.write(buffer, 0, byteread);
                    }
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    public static void saveByTjBitmap(TJBitmap tjBitmap) {
        switch (tjBitmap.getInfo().getType()) {
            case 0:
                copyFromTemp("rgba" + tjBitmap.getInfo().getTitle() + JPG);
                break;
            case 1:
                copyFromTemp("rgba" + tjBitmap.getInfo().getTitle() + PNG);
                break;
            case 2:
                copyFromTemp("rgba" + tjBitmap.getInfo().getTitle() + JPG);
                copyFromTemp("mask" + tjBitmap.getInfo().getTitle() + JPG);
                savePNGBitmap(tjBitmap.getShowBitmap(), tjBitmap.getInfo().getTitle());
                break;
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
