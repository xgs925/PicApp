package com.xcommon.view.layer;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.xcommon.R;
import com.xcommon.utils.common.DimenUtil;

import java.util.LinkedList;

/**
 * Created by Photostsrs on 2017/3/6.
 */

public class FrameView extends RelativeLayout {
    private int mBtnSize = 24;//dp
    private int MIN_SIZE = 50;//dp
    private LinkedList<View> mBtnList = new LinkedList<>();
    private Point mZeroPoint;
    private LayerFrameLayout.OnBtnClickListener mOnBtnClickListener;
    private ImageView mStyleBtn;
    private ImageView mDelBtn;
    private ImageView mMoveBtn;


    public void setOnBtnClickListener(LayerFrameLayout.OnBtnClickListener mOnBtnClickListener) {
        this.mOnBtnClickListener = mOnBtnClickListener;
    }

    public FrameView(Context context, Point zeroPoint) {
        super(context);
        mZeroPoint = zeroPoint;
        init();
    }

    private void init() {
        mBtnSize = DimenUtil.dip2px(mBtnSize);
        MIN_SIZE= DimenUtil.dip2px(MIN_SIZE);
        FrameLayout frameLayout = new FrameLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(mBtnSize / 2, mBtnSize / 2, mBtnSize / 2, mBtnSize / 2);
        frameLayout.setLayoutParams(layoutParams);
//        frameLayout.setBackgroundResource(R.drawable.choose_layer_boader);
        addView(frameLayout);
        addDelBtn();
        addStyleBtn();
        addMoveBtn();
    }

    private void addStyleBtn() {
         mStyleBtn = new ImageView(getContext());
//        mStyleBtn.setImageResource(R.mipmap.layer_invert_btn_icon);
        LayoutParams lp = new LayoutParams(mBtnSize, mBtnSize);
        lp.addRule(ALIGN_PARENT_END);
        lp.addRule(ALIGN_PARENT_TOP);
        mStyleBtn.setLayoutParams(lp);
        addView(mStyleBtn);
        mStyleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBtnClickListener == null) return;
                mOnBtnClickListener.onClickStyle();
            }
        });
        mBtnList.add(mStyleBtn);
    }

    private void addDelBtn() {
         mDelBtn = new ImageView(getContext());
//        mDelBtn.setImageResource(R.mipmap.layer_del_btn_icon);
        LayoutParams delLP = new LayoutParams(mBtnSize, mBtnSize);
        delLP.addRule(ALIGN_PARENT_LEFT);
        delLP.addRule(ALIGN_PARENT_TOP);
        mDelBtn.setLayoutParams(delLP);
        addView(mDelBtn);
        mDelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBtnClickListener == null) return;
                mOnBtnClickListener.onClickDel();
            }
        });
        mBtnList.add(mDelBtn);
    }
    private void addMoveBtn() {
        mMoveBtn = new ImageView(getContext());
//        mMoveBtn.setImageResource(R.mipmap.layer_move_brn_icon);
        LayoutParams lp = new LayoutParams(mBtnSize, mBtnSize);
        lp.addRule(ALIGN_PARENT_END);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        mMoveBtn.setLayoutParams(lp);
        addView(mMoveBtn);
        mMoveBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mOnBtnClickListener.onTouchMove();
                return false;
            }
        });
        mBtnList.add(mMoveBtn);
    }
    private void setFrameSize(int width, int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        int w = width + mBtnSize;
        int h = height + mBtnSize;
        if (w < MIN_SIZE) w = MIN_SIZE;
        if (h < MIN_SIZE) h = MIN_SIZE;
        lp.width = w;
        lp.height = h;
        setLayoutParams(lp);
    }

    public void showByLayerView(LayerView view) {
        int w = view.getLayerWidth();
        int h = view.getLayerHeight();
        setFrameSize(w, h);

        Point point = view.getOffsetPoint();
        int x = mZeroPoint.x + point.x - getLayoutParams().width / 2;
        int y = mZeroPoint.y + point.y - getLayoutParams().height / 2;
        setX(x);
        setY(y);

        setRotation(view.getRotation());

        if(view.isLock()){
            mDelBtn.setVisibility(GONE);
            mMoveBtn.setVisibility(GONE);
            mStyleBtn.setVisibility(GONE);
        }else {
            mDelBtn.setVisibility(VISIBLE);
            mMoveBtn.setVisibility(VISIBLE);
            mStyleBtn.setVisibility(VISIBLE);
        }
    }
}
