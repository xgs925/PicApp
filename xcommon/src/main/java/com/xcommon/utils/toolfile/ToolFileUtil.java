package com.xcommon.utils.toolfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
public class ToolFileUtil {
    public static String TAG = "ToolFileUtil";
    public static String TOOL_FOLDER = "/tool/";
    private static Context appContext;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
        createFolderIfNotExist();
    }

    public static String getSystemTimeMillis() {
        return System.currentTimeMillis() + "";
    }

    public static String getToolDir(Context context) {
        return context.getDir("tj", Context.MODE_PRIVATE) + TOOL_FOLDER;
    }

    public static String getToolDir() {
        return appContext.getDir("tj", Context.MODE_PRIVATE) + TOOL_FOLDER;
    }

    public static String encryptName(String name) {
        return name;
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(name.getBytes());
//            byte[] encryption = md5.digest();
//            StringBuffer strBuf = new StringBuffer();
//            for (int i = 0; i < encryption.length; i++) {
//                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
//                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
//                } else {
//                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
//                }
//            }
//            return strBuf.toString();
//        } catch (NoSuchAlgorithmException e) {
//            return null;
//        }
    }

    public static boolean isExist(Context context, String fileName) {
        File file = new File(getToolDir(context) + encryptName(fileName));
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
        File file = new File(getToolDir(context));
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {

            }
        }
    }

    public static void createFolderIfNotExist() {
        File file = new File(getToolDir());
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {

            }
        }
    }

    public static void clearTemp(Context context, List<String> keepFileNames) {
        File tool = new File(getToolDir());
        String[] toolFiles = tool.list();
        if (keepFileNames == null) {
            deleteFile(tool);
        } else {
            for (String toolFile : toolFiles) {
                if (!keepFileNames.contains(toolFile))
                    deleteFile(new File(getToolDir(context) + toolFile));
            }
        }
    }

    public static void clearTemp(Context context) {
        clearTemp(context, null);
    }

    public static void deleteJPGByName(Context context, String name) {
        deleteFile(new File(getToolDir(context) + encryptName(name + ".jpg")));
    }

    public static void deletePNGByName(Context context, String name) {
        deleteFile(new File(getToolDir(context) + encryptName(name + ".png")));
    }

    public static String saveBitmap2Temp(Context context, Bitmap bitmap, String name) {
        if (bitmap == null) return null;
        createFolderIfNotExist(context);
        File file = new File(getToolDir(context) + encryptName(name + ".jpg"));
        FileOutputStream fOut = null;
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
//            clearTemp(context);
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Bitmap getBitmapByName(Context context, String name) {
        File file = new File(getToolDir(context) + encryptName(name+".jpg"));
        if (!file.exists()) {
            Log.e(TAG, getToolDir(context) + name + "不存在！");
            return null;
        }
        return decodeBitmapFile(file.getAbsolutePath());
    }

    public static String saveBitmap2TempPNG(Context context, Bitmap bitmap, String name) {
        if (bitmap == null) return null;
        createFolderIfNotExist(context);
        File file = new File(getToolDir(context) + encryptName(name + ".png"));
        FileOutputStream fOut = null;
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static String saveBitmap2TempPNG(Bitmap bitmap, String name) {
        if (bitmap == null) return null;
        createFolderIfNotExist();
        File file = new File(getToolDir() + encryptName(name + ".png"));
        FileOutputStream fOut = null;
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Bitmap getPNGBitmapInTempByName(Context context, String name) {
        String path = getToolDir(context) + encryptName(name + ".png");
        Bitmap bitmap = ToolBitmapCache.getInstance().getBitmap(path);
        if (bitmap != null) return bitmap;
        File file = new File(path);
        if (!file.exists()) {
            LogUtil.e(TAG, name + ".png 不存在！");
            return null;
        }
        return decodeBitmapFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmapInTempByNameWithSuffix(Context context, String name) {
        String path = getToolDir(context) + encryptName(name);
        Bitmap bitmap = ToolBitmapCache.getInstance().getBitmap(path);
        if (bitmap != null) return bitmap;
        File file = new File(path);
        if (!file.exists()) {
            LogUtil.e(TAG, name + "不存在！");
            return null;
        }
        return decodeBitmapFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmapInTempByName(Context context, String name) {
        String path = getToolDir(context) + encryptName(name + ".jpg");
        Bitmap bitmap = ToolBitmapCache.getInstance().getBitmap(path);
        if (bitmap != null) return bitmap;
        File file = new File(path);
        if (!file.exists()) {
            LogUtil.e(TAG, name + ".jpg 不存在！");
            return null;
        }
        return decodeBitmapFile(file.getAbsolutePath());
    }

    public static Bitmap decodeBitmapFile(String pathName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public static boolean renameFile(Context context, String orName, String newName) {
        String oldPath = getToolDir(context) + encryptName(orName);
        File orFile = new File(oldPath);
        if (!orFile.exists()) return false;
        String newPath = getToolDir(context) + encryptName(newName);
        File newFile = new File(newPath);
        orFile.renameTo(newFile);
        return true;
    }

    public static boolean copyFile(Context context, String orName, String newName) {
        File orFile = new File(getToolDir(context) + encryptName(orName));
        if (!orFile.exists()) return false;
        File newFile = new File(getToolDir(context) + encryptName(newName));
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
        return true;
    }


    public static void saveBitmap2Temp(final Bitmap bitmap, String name) {
        if (bitmap == null) return;
        final String path = getToolDir() + encryptName(name + ".jpg");
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

    public static void savePNGBitmap2Temp(final Bitmap bitmap, String name) {
        if (bitmap == null || bitmap.isRecycled()) return;
        final String path = getToolDir() + encryptName(name + ".png");
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
                    if (!bitmap.isRecycled()) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    }
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

    public static Bitmap getPNGBitmapInTempByName(String name) {
        String path = getToolDir() + encryptName(name + ".png");
        Bitmap bitmap = ToolBitmapCache.getInstance().getBitmap(path);
        if (bitmap != null) return bitmap;
        File file = new File(path);
        if (!file.exists()) {
            LogUtil.e(TAG, name + ".png 不存在！");
            return null;
        }
        return decodeBitmapFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmapInTempByNameWithSuffix(String name) {
        String path = getToolDir() + encryptName(name);
        Bitmap bitmap = ToolBitmapCache.getInstance().getBitmap(path);
        if (bitmap != null) return bitmap;
        File file = new File(path);
        if (!file.exists()) {
            LogUtil.e(TAG, name + "不存在！");
            return null;
        }
        return decodeBitmapFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmapInTempByName(String name) {
        String path = getToolDir() + encryptName(name + ".jpg");
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

    public static void deleteJPGByName(String name) {
        deleteFile(new File(getToolDir() + encryptName(name + ".jpg")));
    }

    public static void deletePNGByName(String name) {
        deleteFile(new File(getToolDir() + encryptName(name + ".png")));
    }

    public static boolean renameFile(String orName, String newName) {
        String oldPath = getToolDir() + encryptName(orName);
        File orFile = new File(oldPath);
        if (!orFile.exists()) return false;
        String newPath = getToolDir() + encryptName(newName);
        File newFile = new File(newPath);
        orFile.renameTo(newFile);
        ToolBitmapCache.getInstance().renameBitmap(oldPath, newPath);
        return true;
    }

    public static boolean copyFile(String orName, String newName) {
        String oldPath = getToolDir() + encryptName(orName);
        final File orFile = new File(oldPath);
        if (!orFile.exists()) return false;
        final String newPath = getToolDir() + encryptName(newName);
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
}
