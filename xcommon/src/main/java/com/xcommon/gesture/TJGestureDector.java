package com.xcommon.gesture;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by Photostsrs on 2017/3/6.
 */

public class TJGestureDector {
    private Context mContext;
    private MoveGestureDetector mMoveGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private RotateGestureDetector mRotateGestureDetector;
    private MoveListener mMoveListener;
    private ScaleListener mScaleListener;
    private RotateListener mRotateListener;

    public TJGestureDector(Context context) {
        this.mContext = context;
    }

    public void setMoveListener(MoveListener moveListener) {
        this.mMoveListener = moveListener;
        mMoveGestureDetector=new MoveGestureDetector(mContext,new MoveGestureListener());
    }

    public void setScaleListener(ScaleListener scaleListener) {
        this.mScaleListener = scaleListener;
        mScaleGestureDetector=new ScaleGestureDetector(mContext,new ScaleGestureListener());
    }

    public void setRotateListener(RotateListener rotateListener) {
        this.mRotateListener = rotateListener;
        mRotateGestureDetector=new RotateGestureDetector(mContext,new RotateGestureListener());
    }

    public interface MoveListener {
        void onMove(float x, float y);
    }

    public interface ScaleListener {
        void onScale(float factor);
    }

    public interface RotateListener {
        void onRotate(float rotation);
    }

    public void onTouchEvent(MotionEvent event) {
        if (mMoveGestureDetector != null) mMoveGestureDetector.onTouchEvent(event);
        if (mScaleGestureDetector != null) mScaleGestureDetector.onTouchEvent(event);
        if (mRotateGestureDetector != null) mRotateGestureDetector.onTouchEvent(event);
    }

    class MoveGestureListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            mMoveListener.onMove(d.x,d.y);
            return true;
        }
    }

    class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float factor = detector.getScaleFactor();
            mScaleListener.onScale(factor);
            return true;
        }
    }

    class RotateGestureListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            float rotation=detector.getRotationDegreesDelta();
            mRotateListener.onRotate(rotation);
            return true;
        }
    }
}
