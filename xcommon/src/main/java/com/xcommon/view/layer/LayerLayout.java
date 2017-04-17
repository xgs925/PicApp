package com.xcommon.view.layer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xcommon.gesture.TJGestureDector;
import com.xcommon.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Photostsrs on 2016/11/14.
 */
public class LayerLayout extends RelativeLayout {
    private boolean mSquare;//工作区是否为正方形
    private FrameLayout mWorkLayout;
    private LayerFrameLayout mLayerFrameLayout;
    private List<LayerView> layerViews = new ArrayList<>();
    private LayerView curLayer;
    private int layerLayoutWidth;
    private int layerLayoutHeight;
    private int availableWidth;
    private int mBgHeight;
    private int mBgWidth;
    private int availableHeight;
    private float bgScale;
    private int curIndex = -1;
    private int chooseType = 0;//-1 未选中 0 选中背景 1 选中单个图层 2 选中多个图层
    private int mBgType = 0;//0:透明 1:图片 2:纯色
    private Bitmap mBgBitmap;
    private int mBgColor = Color.WHITE;

    private Point oPoint = new Point();
    private int MAX_LAYER = 20;
    private LinkedList<LayerView> mDownEventLayers = new LinkedList<>();

    //////////////////////listener start//////////////////////
    private OnDoubleTapLayerListener mOnDoubleTapLayerListener;

    public void setOnDoubleTapLayerListener(OnDoubleTapLayerListener mOnDoubleTapLayerListener) {
        this.mOnDoubleTapLayerListener = mOnDoubleTapLayerListener;
    }

    public interface OnDoubleTapLayerListener {
        void onDoubleTap(LayerView layerView);
    }


    private LayerListener layerListener;

    public void setLayerListener(LayerListener layerListener) {
        this.layerListener = layerListener;
    }

    public interface LayerListener {
        void haveLayerChoosed();

        void chooseNoLayer();

        void onTextStyleClick();
    }

    public void setOnLayerChangeListener(OnLayerChangeListener onLayerChangeListener) {
        this.onLayerChangeListener = onLayerChangeListener;
    }

    private OnLayerChangeListener onLayerChangeListener;

    public interface OnLayerChangeListener {
        void onChooseLayer(int index);

        void onChooseNoLayer();

        void onPreStatus();

        void onDelLayer();
    }

    //////////////////////listener end//////////////////////
    public LayerLayout(Context context) {
        super(context);
        init(context);
    }

    public LayerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LayerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LayerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mSquare = false;
        init(context);
    }

    private void init(Context context) {
        mWorkLayout = new FrameLayout(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mWorkLayout.setLayoutParams(lp);
        addView(mWorkLayout);
        mLayerFrameLayout = new LayerFrameLayout(getContext());
        mLayerFrameLayout.setOnBtnClickListener(new LayerFrameLayout.OnBtnClickListener() {
            @Override
            public void onClickDel() {
                delLayer(curLayer.getIndex());
                chooseNoLayer();
                onLayerChangeListener.onDelLayer();
            }

            @Override
            public void onClickStyle() {
                getCurLayer().leftRightInvertView();
            }

            @Override
            public void onTouchMove() {
                mTouchMove = true;
            }
        });
        addView(mLayerFrameLayout);
        gestureDetector = new GestureDetector(context, new GestureListener());
        mTjGestureDector = new TJGestureDector(getContext());
        mTjGestureDector.setMoveListener(new LayerMoveListener());
        mTjGestureDector.setScaleListener(new LayerScaleListener());
        mTjGestureDector.setRotateListener(new LayerRotateListener());
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                availableWidth = getMeasuredWidth();
                availableHeight = getMeasuredHeight();
                if (mSquare) {
                    if (availableWidth > availableHeight) availableWidth = availableHeight;
                    else availableHeight = availableWidth;
                    mWorkLayout.getLayoutParams().width = availableWidth;
                    mWorkLayout.getLayoutParams().height = availableHeight;
                }
                layerLayoutWidth = availableWidth;
                layerLayoutHeight = availableHeight;
                oPoint.x = layerLayoutWidth / 2;
                oPoint.y = layerLayoutHeight / 2;
                if (mBgWidth != 0) resetLayerLayout(mBgWidth, mBgHeight);
                return true;
            }
        });
    }

    public void resetLayerLayout(int bgWidth, int bgHeight) {
        setVisibility(View.VISIBLE);
        if(availableWidth==0) return;
        if (bgWidth * 1.0f / bgHeight > availableWidth * 1.0f / availableHeight) {//水平充满
            layerLayoutWidth = availableWidth;
            layerLayoutHeight = (int) (layerLayoutWidth / (bgWidth * 1.0f / bgHeight));
            bgScale = layerLayoutHeight * 1.0f / bgHeight;
        } else {
            layerLayoutHeight = availableHeight;
            layerLayoutWidth = (int) (layerLayoutHeight * (bgWidth * 1.0f / bgHeight));
            bgScale = layerLayoutWidth * 1.0f / bgWidth;
        }
        mWorkLayout.getLayoutParams().width = layerLayoutWidth;
        mWorkLayout.getLayoutParams().height = layerLayoutHeight;
        oPoint.x = layerLayoutWidth / 2;
        oPoint.y = layerLayoutHeight / 2;
        mBgWidth = bgWidth;
        mBgHeight = bgHeight;

        for (LayerView layerView : getLayerViews()) {
            layerView.setoPoint(oPoint);
            layerView.offsetTo(layerView.getOffsetPoint());
        }
    }

    //////////////////////setter getter start///////////////////////////////
    public int getMaxLayer(){
        return MAX_LAYER;
    }
    public void setBgSize(int w, int h) {
        mBgWidth = w;
        mBgHeight = h;
    }

    public int getChooseType() {
        return chooseType;
    }

    public void setChooseType(int type) {
        chooseType = type;
    }

    public void setWorkLayoutBackgroudNull(Bitmap bitmap) {
        mBgColor = 0;
        mBgType = 0;
        mWorkLayout.setBackground(new BitmapDrawable(bitmap));
    }

    public void setWorkLayoutBackgroud(Bitmap bitmap) {
        mBgColor = -10;
        mBgType = 1;
        mBgBitmap = bitmap;
        mWorkLayout.setBackground(new BitmapDrawable(mBgBitmap));
    }

    public void setWorkLayoutBackgroudColor(int color) {
        mBgColor = color;
        if (color == Color.TRANSPARENT) {
        } else {
            mBgType = 2;
            mWorkLayout.setBackgroundColor(color);
        }
        recycleBgBitmap();
    }

    public int getLayerLayoutWidth() {
        return layerLayoutWidth;
    }

    public int getLayerLayoutHeight() {
        return layerLayoutHeight;
    }

    public List<LayerView> getLayerViews() {
        return layerViews;
    }

    public LayerView getCurLayer() {
        if (!isChooseLayer()) return null;
        return curLayer;
    }

    public int getBgColor() {
        return mBgColor;
    }

    //////////////////////setter getter end///////////////////////////////
    public TextLayerView addText() {
        TextLayerView layerView = newTextLayerView();
        addLayer(layerView, false);
        return layerView;
    }
    public TextLayerView addText(TextView textView) {
        TextLayerView layerView = new TextLayerView(getContext(), textView);
        addLayer(layerView, false);
        return layerView;
    }

    public TextLayerView addText(String text) {
        TextLayerView layerView = new TextLayerView(getContext(), text);
        addLayer(layerView, false);
        return layerView;
    }

    public LayerView addBitmapWithoutParas(Bitmap bitmap) {
        LayerView layerView = newImageLayerView(bitmap);
        addLayer(layerView, true);
        return layerView;
    }

    public LayerView addBitmapWithParas(Bitmap bitmap, int width, int height, Point offsetPoint, float rotation, boolean lock) {
        LayerView layerView = newImageLayerView(bitmap);
        if (lock)
            layerView.lockLayer();
        addLayer(layerView, false);

        layerView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        layerView.setOffsetPoint(offsetPoint);
        layerView.setRotation(rotation);
        updateFrame();
        return layerView;
    }

    private LayerView newImageLayerView(Bitmap layerBitmap) {
        ImageLayerView layerView = new ImageLayerView(getContext());
        layerView.setImageBitmap(layerBitmap);
        return layerView;
    }

    private TextLayerView newTextLayerView() {
        TextLayerView layerView = newTextLayerView(null);
        return layerView;
    }

    private TextLayerView newTextLayerView(TextView textView) {
        TextLayerView layerView = null;
        if (textView != null) {
            layerView = new TextLayerView(getContext(), textView);
        } else {
            layerView = new TextLayerView(getContext());
        }
        return layerView;
    }

    private void addLayer(LayerView layerView, boolean needAjustSize) {
        addInLayerLayout(layerView, needAjustSize);
    }

    private void addInLayerLayout(LayerView layerView, boolean needAjustSize) {
        // TODO: 2017/3/31 优化
        Point size = getLayerViewSize(layerView.getBitmap());
        if (needAjustSize) {
            ajustLayerSize(layerView, size.x, size.y);
        }
        layerView.setWRH(size.x * 1f / size.y);
        layerView.setoPoint(oPoint);
        layerView.setLayerLayoutW(layerLayoutWidth);
        layerView.setLayerLayoutH(layerLayoutHeight);
        addLayer(layerView);
    }

    private void ajustLayerSize(LayerView layerView, int layerViewWidth, int layerViewHeight) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(layerViewWidth, layerViewHeight);
        layerView.setLayoutParams(lp);
    }

    private Point getLayerViewSize(Bitmap layerBitmap) {
        int layerViewWidth;
        int layerViewHeight;
        float photoWidth = layerBitmap.getWidth();
        float photoHeight = layerBitmap.getHeight();
        if (photoWidth < layerLayoutWidth / 2 & photoHeight < layerLayoutHeight / 2) {
            layerViewWidth = (int) photoWidth;
            layerViewHeight = (int) photoHeight;
        } else {
            if (photoWidth / photoHeight > layerLayoutWidth / layerLayoutHeight) {//水平充满
                layerViewWidth = layerLayoutWidth / 2;
                layerViewHeight = (int) (layerViewWidth / (photoWidth / photoHeight));
            } else {
                layerViewHeight = layerLayoutHeight / 2;
                layerViewWidth = (int) (layerViewHeight * (photoWidth / photoHeight));
            }
        }
        Point point = new Point(layerViewWidth, layerViewHeight);
        return point;
    }

    /////////////////////event start//////////////////////////
    private GestureDetector gestureDetector;
    private TJGestureDector mTjGestureDector;
    private boolean moved = false;
    private boolean exeScaleEvent;
    private boolean mTouchMove;

    private void resetGestureParas(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP) return;
        if (exeScaleEvent) {
            exeScaleEvent = false;
            if (isChooseLayer()) curLayer.doneTowFingleEvent();
        }
        mTouchMove = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        checkListener(event);
        resetGestureParas(event);
        gestureDetector.onTouchEvent(event);
        if (!isChooseLayer()) return true;
        mTjGestureDector.onTouchEvent(event);
        mLayerFrameLayout.showByLayerView(curLayer);
        return true;
    }

    private void checkListener(MotionEvent event) {
        if (isChooseLayer() && curLayer.getOnLayerChangeListener() != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    curLayer.getOnLayerChangeListener().onChangeStart();
                    break;
                case MotionEvent.ACTION_UP:
                    if (moved)
                        curLayer.getOnLayerChangeListener().onChangeEnd();
                    moved = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    moved = true;
                    curLayer.getOnLayerChangeListener().onChange();
                    break;
            }
        }
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        float oRotation, oD;

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mDownEventLayers.size() == 0) {
                chooseNoLayer();
                onLayerChangeListener.onChooseNoLayer();
            } else if (isChooseLayer()) {
                boolean isChoosed = false;
                boolean find = false;
                for (LayerView layerView : mDownEventLayers
                        ) {
                    if (find) {
                        chooseLayer(layerView.getIndex());
                        isChoosed = true;
                        break;
                    }
                    if (curLayer.getIndex() == layerView.getIndex()) find = true;
                }
                if (!isChoosed) chooseLayer(mDownEventLayers.get(0).getIndex());
                onLayerChangeListener.onChooseLayer(curIndex);
            } else {
                chooseLayer(mDownEventLayers.get(0).getIndex());
                onLayerChangeListener.onChooseLayer(curIndex);
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mOnDoubleTapLayerListener == null) return true;
            if (!isChooseLayer()) return true;
            mOnDoubleTapLayerListener.onDoubleTap(getCurLayer());
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if (mTouchMove) {
                oRotation = (float) Math.toDegrees(Math.atan2(curLayer.getHeight(), curLayer.getWidth()));
                oD = (float) (Math.sqrt(curLayer.getHeight() * curLayer.getHeight() + curLayer.getWidth() * curLayer.getWidth()) / 2);
            }
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!mTouchMove || oD == 0) return false;
            float x = e2.getX() - mWorkLayout.getX() - oPoint.x - curLayer.getOffsetPoint().x;
            float y = e2.getY() - mWorkLayout.getY() - oPoint.y - curLayer.getOffsetPoint().y;
            float rotation = (float) Math.toDegrees(Math.atan2(y, x));
            curLayer.setRotation(rotation - oRotation);
            float d = (float) (Math.sqrt(x * x + y * y));
            float scale = d / oD;
            curLayer.scaleByScale(scale);
            return true;
        }
    }

    class LayerMoveListener implements TJGestureDector.MoveListener {
        @Override
        public void onMove(float x, float y) {
            if (mTouchMove) return;
            Point offsetPoint = curLayer.getOffsetPoint();
            offsetPoint.x = (int) (offsetPoint.x + x);
            offsetPoint.y = (int) (offsetPoint.y + y);
            if (offsetPoint.x < -layerLayoutWidth / 2) offsetPoint.x = -layerLayoutWidth / 2;
            if (offsetPoint.x > layerLayoutWidth / 2) offsetPoint.x = layerLayoutWidth / 2;
            if (offsetPoint.y < -layerLayoutHeight / 2) offsetPoint.y = -layerLayoutHeight / 2;
            if (offsetPoint.y > layerLayoutHeight / 2) offsetPoint.y = layerLayoutHeight / 2;
            curLayer.setOffsetPoint(offsetPoint);
        }
    }

    class LayerScaleListener implements TJGestureDector.ScaleListener {

        @Override
        public void onScale(float factor) {
            if (mTouchMove) return;
            exeScaleEvent = true;
            curLayer.scaleByFactor(factor);
        }
    }

    class LayerRotateListener implements TJGestureDector.RotateListener {
        @Override
        public void onRotate(float rotation) {
            if (mTouchMove) return;
            curLayer.setRotation(curLayer.getRotation() - rotation);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mDownEventLayers.clear();
        return super.onInterceptTouchEvent(ev);
    }

    /////////////////////event end//////////////////////////
    public void updateFrame() {
        mLayerFrameLayout.showByLayerView(curLayer);
    }

    public void updateBitmap(Bitmap bitmap) {
        if (curLayer != null) {
            curLayer.updateImageBitmap(bitmap);
            updateFrame();
        }
    }

    private boolean isChooseLayer() {
        if (curLayer == null) return false;
        if (curLayer.isLock()) return false;
        return true;
    }

    private void recycleBgBitmap() {
        if (mBgBitmap == null) return;
        if (!mBgBitmap.isRecycled()) mBgBitmap.recycle();
        mBgBitmap = null;
    }

    public void chooseLayer(int index) {
        if(layerViews.get(index).isLock()) {
            mLayerFrameLayout.showByLayerView(layerViews.get(index));
            return;
        }
        curLayer = layerViews.get(index);
        curIndex = index;
        if (layerListener != null) layerListener.haveLayerChoosed();
        mLayerFrameLayout.showByLayerView(curLayer);
    }

    public void chooseNoLayer() {
        mLayerFrameLayout.hideAllFrame();
        if (curLayer != null) {
            curIndex = -1;
            curLayer = null;
        }
        if (layerListener != null) layerListener.chooseNoLayer();
    }

    private void addLayer(LayerView layerView) {
        if (layerViews.size() >= MAX_LAYER) {
            Toast.makeText(getContext(), "图层数量到达上限", Toast.LENGTH_SHORT).show();
            return;
        }
        layerView.setDownEventListener(new LayerView.DownEventListener() {
            @Override
            public void onDown(LayerView view) {
                mDownEventLayers.add(view);
            }
        });

        mWorkLayout.addView(layerView, layerViews.size());
        layerView.setIndex(layerViews.size());
        layerViews.add(layerView);

        layerView.setOffsetPoint(new Point(0, 0));
        chooseLayer(layerView.getIndex());
    }

    public void delLayer(int index) {
        if (index >= layerViews.size()) return;
        mWorkLayout.removeView(layerViews.get(index));
        layerViews.remove(index);
        chooseNoLayer();
        if (index < layerViews.size())
            for (int i = index; i < layerViews.size(); i++) {
                LayerView layer = layerViews.get(i);
                layer.setIndex(layer.getIndex() - 1);
            }
    }

    public void moveUpLayer() {
        if (curLayer == null) return;
        if (curIndex >= layerViews.size() - 1) return;
        curLayer.setIndex(curIndex + 1);
        layerViews.get(curIndex + 1).setIndex(curIndex);
        Collections.swap(layerViews, curIndex, curIndex + 1);
        mWorkLayout.removeView(curLayer);
        mWorkLayout.addView(curLayer, curIndex + 1);
        curIndex += 1;
    }

    public void moveLayerTo(int index) {
        if (curLayer == null) return;
        if (curIndex == index) return;
        curLayer.setIndex(index);
        layerViews.get(index).setIndex(curIndex);
        Collections.swap(layerViews, curIndex, index);
        mWorkLayout.removeView(curLayer);
        mWorkLayout.addView(curLayer, index);
        curIndex = index;
    }

    public void moveDownLayer() {
        if (curLayer == null) return;
        if (curIndex <= 0) return;
        curLayer.setIndex(curIndex - 1);
        layerViews.get(curIndex - 1).setIndex(curIndex);
        Collections.swap(layerViews, curIndex, curIndex - 1);
        mWorkLayout.removeView(curLayer);
        mWorkLayout.addView(curLayer, curIndex - 1);
        curIndex -= 1;
    }

    public boolean isCurrentTextLayer() {
        if (!isChooseLayer()) return false;
        if (getCurLayer() instanceof TextLayerView) return true;
        return false;
    }

    public boolean isCurrentImageLayer() {
        if (getCurLayer() == null) return false;
        if (getCurLayer() instanceof ImageLayerView) return true;
        return false;
    }

    public Bitmap getResultBitmap() {
        return getResultBitmapByLayerIndex(layerViews.size(), layerLayoutWidth);
    }

    public Bitmap getResultBitmap(int sizeW) {
        return getResultBitmapByLayerIndex(layerViews.size(), sizeW);
    }

    public Bitmap getResultBitmapByLayerIndex(int index) {
        return getResultBitmapByLayerIndex(index,layerLayoutWidth);
    }

    public Bitmap getResultBitmapByLayerIndex(int index, int sizeW) {
        float drawBgScale = sizeW * 1f / layerLayoutWidth;
        int sizeH = (int) (sizeW * (layerLayoutHeight * 1.0f / layerLayoutWidth));
        Bitmap newBitmap = Bitmap.createBitmap(sizeW, sizeH, Bitmap.Config.ARGB_8888);
        switch (mBgType) {
            case 1:
                Canvas bcanvas = new Canvas(newBitmap);
                Paint bPaint = new Paint();
                bPaint.setFilterBitmap(true);
                bPaint.setAntiAlias(true);
                bPaint.setDither(true);
                bcanvas.drawBitmap(mBgBitmap, null, new RectF(0, 0, sizeW, sizeH), null);
                break;
            case 2:
                Canvas ccanvas = new Canvas(newBitmap);
                ccanvas.drawColor(mBgColor);
                break;
        }
        for (int i = 0; i < index; i++) {
            LayerView layerView = layerViews.get(i);
            Bitmap bitmap = layerView.getBitmap();
            int viewWidth = layerView.getLayerWidth();
            int bitmapWidth = bitmap.getWidth();
            float scale = (viewWidth * drawBgScale) * 1f / bitmapWidth;
            Point translate = new Point((int) (layerView.getOffsetPoint().x * drawBgScale), (int) (layerView.getOffsetPoint().y * drawBgScale));
            newBitmap = BitmapUtil.blendTowBitmap(newBitmap, bitmap, layerView.getRotation(), scale, translate, layerView.getLayerAlpha(), layerView.isInvert());
        }
        return newBitmap;
    }

    @Override
    public void removeAllViews() {
        mWorkLayout.removeAllViews();
        layerViews.clear();
    }
}
