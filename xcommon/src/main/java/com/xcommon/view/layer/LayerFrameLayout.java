package com.xcommon.view.layer;

import android.content.Context;
import android.graphics.Point;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Created by Photostsrs on 2017/3/6.
 */

public class LayerFrameLayout extends FrameLayout {
    private FrameView mFrameView;
    private Point mZeroPoint;
    private OnBtnClickListener mOnBtnClickListener;
    public void setOnBtnClickListener(OnBtnClickListener mOnBtnClickListener) {
        this.mOnBtnClickListener=mOnBtnClickListener;
    }
    public interface OnBtnClickListener{
        void onClickDel();
        void onClickStyle();
        void onTouchMove();
    }
    public LayerFrameLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        setVisibility(INVISIBLE);//不能为Gone 否则 getMeasuredWidth()= 0
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                mZeroPoint = new Point(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                mFrameView = new FrameView(getContext(), mZeroPoint);
                addView(mFrameView);
                mFrameView.setOnBtnClickListener(mOnBtnClickListener);
                return true;
            }
        });
    }

    public void showByLayerView(LayerView view) {
        setVisibility(VISIBLE);
        mFrameView.showByLayerView(view);
    }

    public void hideAllFrame() {
        setVisibility(GONE);
    }
}
