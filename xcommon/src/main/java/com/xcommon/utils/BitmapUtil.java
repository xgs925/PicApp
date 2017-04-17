package com.xcommon.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;


/**
 * Created by Photostsrs on 2016/5/11.
 */
public class BitmapUtil {
    public static void showTestBitmap(Activity activity,Bitmap bitmap){
        ImageView imageView=new ImageView(activity);
        imageView.setImageBitmap(bitmap);
        FrameLayout layout = (FrameLayout) activity.findViewById(android.R.id.content);
        layout.addView(imageView);
    }
    public static Bitmap drawSubByRect(Bitmap orBitmap,Bitmap subBitmap, Rect rect){
        Bitmap resultBitmap=Bitmap.createBitmap(orBitmap);
        Canvas canvas=new Canvas(resultBitmap);
        canvas.drawBitmap(subBitmap,null,rect,null);
        return resultBitmap;
    }
    public static Bitmap cropBitmapByRect(Bitmap bitmap, Rect rect){
        Bitmap resultBitmap=Bitmap.createBitmap(bitmap,rect.left,rect.top,rect.width(),rect.height());
        return resultBitmap;
    }
    public static void addMark(Bitmap bitmap, Bitmap mark){
        int bmpW=bitmap.getWidth();
        int markW=mark.getWidth();
        int needMarkW= (int) Math.ceil((145f/720)*bmpW);
        int needMarkH= (int) (needMarkW*1.0f/markW*mark.getHeight());
        Canvas canvas=new Canvas(bitmap);
        canvas.drawBitmap(mark,null,new RectF(bmpW-needMarkW,0,bmpW,needMarkH),null);
    }

    public static byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static Bitmap resizeImage(Bitmap bitmap, int max) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        if(width<max&height<max) return bitmap;
        float scale;
        if(width>height){
            scale=max*1.0f/width;
        }else {
            scale=max*1.0f/height;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }
    public static Bitmap blendTowBitmap(Bitmap backImage, Bitmap foreImage, float degree, float scale, Point translate){
        return blendTowBitmap(backImage,foreImage,degree,scale,translate,255,false);
    }
    public static Bitmap blendTowBitmap(Bitmap backImage, Bitmap foreImage, float degree, float scale, Point translate, int alpha, boolean isInvert) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Bitmap bitmap = backImage;
        if (!bitmap.isMutable()) {
            bitmap=backImage.copy(Bitmap.Config.ARGB_8888,true);
        }
        Canvas canvas = new Canvas(bitmap);
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

    public static Bitmap scaleBitmap2Show(Bitmap bitmap) {
        if (bitmap == null) return null;
        Point point = TJBTSampleImageInRect(bitmap.getWidth(), bitmap.getHeight());
        if (point.x < bitmap.getWidth()) {
            return Bitmap.createScaledBitmap(bitmap, point.x, point.y, true);
        } else {
            return bitmap;
        }
    }

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
