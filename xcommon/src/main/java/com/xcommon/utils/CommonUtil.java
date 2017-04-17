package com.xcommon.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.Toast;

import com.xcommon.Constant;
import com.xcommon.TJTypeface;
import com.xcommon.utils.common.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Photostsrs on 2016/5/11.
 */
public class CommonUtil {
    public static String TAG = "CommonUtil";

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void lockForOneSec(final View view) {
        if (!view.isClickable()) return;
        view.setClickable(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view == null) return;
                view.setClickable(true);
            }
        }, 1000);
    }

    public static Bitmap blendTowBitmap(Bitmap backImage, Bitmap foreImage, float degree, float scale, Point translate, int alpha, boolean isInvert) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Bitmap bitmap = Bitmap.createBitmap(backImage.getWidth(), backImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(backImage, 0.0F, 0.0F, paint);
        float foreCenterX = (float) foreImage.getWidth() * scale / 2.0F;
        float foreCenterY = (float) foreImage.getHeight() * scale / 2.0F;
        float top = (float) (backImage.getWidth() / 2) - foreCenterX;
        float left = (float) (backImage.getHeight() / 2) - foreCenterY;
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        if (isInvert) matrix.postScale(-1, 1, foreCenterX, foreCenterY);
        matrix.postRotate(degree, foreCenterX, foreCenterY);
        matrix.postTranslate(top + (float) translate.x, left + (float) translate.y);
        paint.setAlpha(alpha);
        canvas.drawBitmap(foreImage, matrix, paint);
        return bitmap;
    }

    public static Bitmap blendTowBitmapBgInvert(Bitmap backImage, Bitmap foreImage, float degree, float scale, Point translate, int alpha, boolean isInvert) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Bitmap bitmap = Bitmap.createBitmap(backImage.getWidth(), backImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        canvas.scale(-1, 1, backImage.getWidth() / 2, backImage.getHeight() / 2);
        canvas.drawBitmap(backImage, 0.0F, 0.0F, paint);
        canvas.restore();
        float foreCenterX = (float) foreImage.getWidth() * scale / 2.0F;
        float foreCenterY = (float) foreImage.getHeight() * scale / 2.0F;
        float top = (float) (backImage.getWidth() / 2) - foreCenterX;
        float left = (float) (backImage.getHeight() / 2) - foreCenterY;
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        if (isInvert) matrix.postScale(-1, 1, foreCenterX, foreCenterY);
        matrix.postRotate(degree, foreCenterX, foreCenterY);
        matrix.postTranslate(top + (float) translate.x, left + (float) translate.y);
        paint.setAlpha(alpha);
        canvas.drawBitmap(foreImage, matrix, paint);
        return bitmap;
    }

    public static byte[] bitmap2Byte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        return bitmapByte;
    }

    public static Bitmap byte2Bitmap(byte[] bitmapByte) {
        return BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
    }

    public static List<TJTypeface> getTypefaces() {
        List<TJTypeface> typefaces = new ArrayList<>();
        String dir = Environment.getExternalStorageDirectory() + Constant.ROOT_FOLDER + "/Fonts/";
        File destDir = new File(dir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String[] fileList = destDir.list();
        for (int i = 0; i < fileList.length; i++) {
            int length = fileList[i].length();
            if (length >= 4) {
                String type = fileList[i].substring(length - 4, length);
                if (type.equalsIgnoreCase(".ttf") | type.equalsIgnoreCase(".otf") | type.equalsIgnoreCase(".ttc")) {
                    Typeface tf = Typeface.createFromFile(destDir + "/" + fileList[i]);
                    typefaces.add(new TJTypeface(tf, fileList[i]));
                }
            }
        }

        return typefaces;
    }

    public static int getWindowWidth(Activity context) {
        return context.getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int getWindowHeight(Activity context) {
        return context.getWindowManager().getDefaultDisplay().getHeight();
    }

    //    public static String insertImage(Context context, ContentResolver cr, Bitmap bm) {
//        String uri = MediaStore.Images.Media.insertImage(cr, bm, "title", "description");
//        String path=getFilePathByContentResolver(context,Uri.parse(uri));
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                    Uri.parse("file://" + path)));
//        return path;
//    }
    private static String getFilePathByContentResolver(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (null == c) {
            throw new IllegalArgumentException(
                    "Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() != 1) || !c.moveToFirst()) {
            } else {
                filePath = c.getString(
                        c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            c.close();
        }
        return filePath;
    }

    public static void delFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void saveMyBitmap(Activity context, Bitmap bitmap) {
        ExternalStorageUtil.savePngBitmap(context,bitmap);
        Toast.makeText(context, "已保存至" + Constant.ROOT_FOLDER, Toast.LENGTH_SHORT).show();
    }

    public static String saveMyBitmap(Activity context, Bitmap bitmap, String bitName) {
//        int REQUEST_EXTERNAL_STORAGE = 1;
//        String[] PERMISSIONS_STORAGE = {
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        };
//        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    context,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }

        String dir = Environment.getExternalStorageDirectory() + Constant.ROOT_FOLDER + "/";
        File destDir = new File(dir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File f = new File(dir + bitName);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("file://" + f.getPath())));
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getPath();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, int percentage) {

        //Let's create an empty bitmap with the same size of the bitmap we want to blur

        Bitmap outBitmap;
        if (percentage > 0 & percentage <= 100) {
            outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            //Instantiate a new Renderscript
            RenderScript rs = RenderScript.create(context);

            //Create an Intrinsic Blur Script using the Renderscript
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
            Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
            Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

            //Set the radius of the blur

            blurScript.setRadius((float) (25.f * (percentage * 1.0 / 100)));

            //Perform the Renderscript
            blurScript.setInput(allIn);
            blurScript.forEach(allOut);

            //Copy the final bitmap created by the out Allocation to the outBitmap
            allOut.copyTo(outBitmap);

            //recycle the original bitmap
//        bitmap.recycle();

            //After finishing everything, we destroy the Renderscript.
            rs.destroy();
        } else {
            outBitmap = bitmap;
        }
        return outBitmap;


    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Point measure(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        return new Point(width, height);
    }

    public static Bitmap scaleBitmap2Show(Bitmap bitmap) {
        if (bitmap == null) {
            LogUtil.e("scaleBitmap2Show null");
            return null;
        }
        Point point = TJBTSampleImageInRect(bitmap.getWidth(), bitmap.getHeight());
        if (point.x < bitmap.getWidth()) {
            Bitmap scaleBitmap=Bitmap.createScaledBitmap(bitmap, point.x, point.y, true);
            bitmap.recycle();
            return scaleBitmap;
        } else {
            return bitmap;
        }
    }

//    public static Bitmap scaleBitmap2Show(Bitmap bitmap) {
//        return scaleBitmap2Show(bitmap, 540);
//    }

    public static Bitmap scaleBitmapLow(Bitmap bitmap) {
        if (bitmap == null) return null;
        Point point = ImageSampleFun(bitmap.getWidth(), bitmap.getHeight(), 2);
        if (point.x < bitmap.getWidth()) {
            return Bitmap.createScaledBitmap(bitmap, point.x, point.y, true);
        } else {
            return bitmap;
        }
    }

    public static Bitmap scaleBitmapStandard(Bitmap bitmap) {
        if (bitmap == null) return null;
        Point point = ImageSampleFun(bitmap.getWidth(), bitmap.getHeight(), 2);
        if (point.x < bitmap.getWidth()) {
            return Bitmap.createScaledBitmap(bitmap, point.x, point.y, true);
        } else {
            return bitmap;
        }
    }

    public static Bitmap scaleBitmapHigh(Bitmap bitmap) {
        if (bitmap == null) return null;
        Point point = ImageSampleFun(bitmap.getWidth(), bitmap.getHeight(), 3);
        if (point.x < bitmap.getWidth()) {
            return Bitmap.createScaledBitmap(bitmap, point.x, point.y, true);
        } else {
            return bitmap;
        }
    }


    // 1: low;  2: standard;  3: high;
    public static Point ImageSampleFun(int orW, int orH, int para) {
        int minStar = 720;
        if (para == 3) minStar = 1080;
        if (para == 1) minStar = 540;
        if (para == 0) minStar = 360;

        int paraBig = 0;//width > height
        int maxLF = orW;
        int minLF = orH;
        if (orH > orW) {
            paraBig = 1;
            maxLF = orH;
            minLF = orW;
        }

        int minLS = minLF;
        int maxLS = maxLF;
        if (minLF > minStar) {
            minLS = minStar;
            maxLS = minStar * maxLF / minLF;
        }

        int minLT = minLS;
        int maxLT = maxLS;

        if (maxLS < 4 * minStar / 3) {
            maxLT = 4 * minStar / 3;
            minLT = 4 * minStar * minLF / (3 * maxLF);
        } else if (maxLS > 16 * minStar / 9) {
            maxLT = 16 * minStar / 9;
            minLT = 16 * minStar * minLF / (9 * maxLF);
        }

        int rsW, rsH;
        if (paraBig == 0) {
            rsW = maxLT;
            rsH = minLT;
        } else {
            rsW = minLT;
            rsH = maxLT;
        }

        if (rsW > orW) rsW = orW;
        if (rsH > orH) rsH = orH;
        Point sizeSample = new Point(rsW, rsH);
        return sizeSample;
    }

    public static Point TJBTSampleImageInRect(int width, int height) {
        return TJBTSampleImageInRect(width, height, 540);
    }

    public static Point TJBTSampleImageInRect(int width, int height, int len) {
        Point imgSize = new Point();
        if (width > height) {
            imgSize.x = len;
            imgSize.y = height * imgSize.x / width;
        } else {
            imgSize.y = len;
            imgSize.x = width * imgSize.y / height;
        }
        if (imgSize.x > width) {
            imgSize.x = width;
            imgSize.y = height;
        }
        return imgSize;
    }
}
