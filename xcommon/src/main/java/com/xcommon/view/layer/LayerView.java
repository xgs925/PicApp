package com.xcommon.view.layer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xcommon.utils.common.DimenUtil;


/**
 * Created by Photostsrs on 2016/6/12.
 */
public class LayerView extends RelativeLayout {
    private String TAG = "LayerView";
    int layerLayoutW;
    int layerLayoutH;
    Point oPoint;
    Point offsetPoint = new Point(0, 0);
    int layerAlpha = 255;
    float wrh;
    protected FrameLayout imageLayout;
    protected ImageView imageView;
    private Bitmap bitmap;
    private int index;
    private boolean isInvert = false;
    protected float mScale = 1;
    private float MIN_SIZE = 5;//dp

    public float getTransparency() {
        return layerAlpha / 255f;
    }

    public void setTransparency(float transparency) {
        setLayerAlpha((int) (transparency * 255));
    }

    public int getLayerAlpha() {
        return layerAlpha;
    }

    public void setLayerAlpha(int layerAlpha) {
        this.layerAlpha = layerAlpha;
        imageView.setAlpha(layerAlpha);
    }

    public Point getOffsetPoint() {
        return offsetPoint;
    }

    public void setOffsetPoint(Point offsetPoint) {
        this.offsetPoint = offsetPoint;
        offsetTo(offsetPoint);
    }

    public void setLayerLayoutW(int layerLayoutW) {
       this.layerLayoutW=layerLayoutW;
    }

    public void setLayerLayoutH(int layerLayoutH) {
        this.layerLayoutH = layerLayoutH;
    }

    public void setLayerWidth(int width){
        scaleByScale(width*1.0f/getWidth());
    }

    public void setWRH(float wrh) {
        this.wrh = wrh;
    }

    public void setoPoint(Point oPoint) {
        this.oPoint = oPoint;
    }

    public Point getoPoint() {
        return oPoint;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean isInvert() {
        return isInvert;
    }

    public void setInvert(boolean invert) {
        isInvert = invert;
        if (isInvert) imageLayout.setScaleX(-1);
    }

    private boolean lock;

    public void lockLayer() {
        lock = true;
    }

    public void unLockLayer() {
        lock = false;
    }

    public boolean isLock() {
        return lock;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }


    public LayerView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        MIN_SIZE = DimenUtil.dip2px(MIN_SIZE);
        imageLayout = new FrameLayout(context);
        LayoutParams llp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageLayout.setLayoutParams(llp);
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lp);
        imageLayout.addView(imageView);
        addView(imageLayout);
    }


    public void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        this.bitmap = bitmap;
    }

    public void updateImageBitmap(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        float bitmapWH = bitmapWidth * 1f / bitmapHeight;
        float viewWH = viewWidth * 1f / viewHeight;
        if (bitmapWH > viewWH) {
            setViewSize(viewWidth, (int) (viewWidth / bitmapWH));
        } else {
            setViewSize((int) (viewHeight * bitmapWH), viewHeight);
        }
        imageView.setImageBitmap(bitmap);
        this.bitmap = bitmap;
    }


    public void setViewSize(int width, int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = width;
        lp.height = height;
        wrh = width * 1f / height;
        setLayoutParams(lp);
        offsetTo(offsetPoint);
    }

    public void scaleByFactor(float factor) {
        float newScale = mScale * factor;
        scaleByScale(newScale);
    }
    public void scaleByScale(float scale) {
        mScale = ajustScale(scale);
        setScaleX(mScale);
        setScaleY(mScale);
        offsetTo(offsetPoint);
    }

    protected float ajustScale(float scale) {
        if (getWidth() * scale < MIN_SIZE) return MIN_SIZE * 1f / getWidth();
        if (getHeight() * scale < MIN_SIZE) return MIN_SIZE * 1f / getHeight();
        return scale;
    }

    protected void offsetTo(Point point) {
        if(oPoint==null) return;
        int x = oPoint.x + point.x - getLayoutParams().width / 2;
        int y = oPoint.y + point.y - getLayoutParams().height / 2;
        setX(x);
        setY(y);
    }

    private OnLayerChangeListener onLayerChangeListener;

    public void setOnLayerChangeListener(OnLayerChangeListener onLayerChangeListener) {
        this.onLayerChangeListener = onLayerChangeListener;
    }

    public OnLayerChangeListener getOnLayerChangeListener() {
        return onLayerChangeListener;
    }

    public interface OnLayerChangeListener {
        void onChangeStart();

        void onChange();

        void onChangeEnd();
    }

    public void leftRightInvertView() {
        if (isInvert) {
            isInvert = false;
            imageLayout.setScaleX(1);
        } else {
            isInvert = true;
            imageLayout.setScaleX(-1);
        }
    }

    public interface DownEventListener {
        void onDown(LayerView view);
    }

    private DownEventListener mDownEventListener;

    public void setDownEventListener(DownEventListener downEventListener) {
        mDownEventListener = downEventListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isLock() && event.getAction() == MotionEvent.ACTION_DOWN) {
            mDownEventListener.onDown(this);
        }
        return super.onTouchEvent(event);
    }
    protected void doneTowFingleEvent(){}
    public int getLayerWidth() {
        return (int) (getLayoutParams().width * getScaleX());
    }

    public int getLayerHeight() {
        return (int) (getLayoutParams().height * getScaleY());
    }
}
