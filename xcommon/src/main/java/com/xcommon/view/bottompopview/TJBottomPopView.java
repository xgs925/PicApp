package com.xcommon.view.bottompopview;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;


import com.xcommon.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Photostsrs on 2016/12/6.
 */

public class TJBottomPopView {
    private Context mContext;
    private TJBottomPopViewAdapter mBottomPopViewAdapter;
    private PopupWindow popupWindow;
    private View mParent;
    private boolean show;
    private int mHight = 200;//px

    public void setAdapter(TJBottomPopViewAdapter bottomPopViewAdapter) {
        this.mBottomPopViewAdapter = bottomPopViewAdapter;
    }

    public void setHight(int hight) {
        this.mHight = hight;
    }

    public TJBottomPopView(Context context, @NonNull View parent) {
        this.mContext = context;
        mParent = parent;
    }

    public void show() {
        if (show) return;
        if (mBottomPopViewAdapter == null) return;
        FrameLayout popupViewLayout = new FrameLayout(mContext);
        popupViewLayout.addView(mBottomPopViewAdapter.getView(mContext));
        popupWindow = new PopupWindow(popupViewLayout, ViewGroup.LayoutParams.MATCH_PARENT, mHight, false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.common_pop_button_anim);
        popupWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        setDismissListener();
        show = true;
    }

    public void showWithoutOutside() {
        showWithoutOutside(false);
    }
    public void showWithoutOutside(boolean focus) {
        if (show) return;
        if (mBottomPopViewAdapter == null) return;
        FrameLayout popupViewLayout = new FrameLayout(mContext);
        popupViewLayout.addView(mBottomPopViewAdapter.getView(mContext));
        popupWindow = new PopupWindow(popupViewLayout, ViewGroup.LayoutParams.MATCH_PARENT, mHight, focus);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.common_pop_button_anim);
        popupWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        setDismissListener();
        show = true;
    }
    public void showWithSoftInput() {
        if (show) return;
        if (mBottomPopViewAdapter == null) return;
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        FrameLayout popupViewLayout = new FrameLayout(mContext);
        popupViewLayout.addView(mBottomPopViewAdapter.getView(mContext));
        popupWindow = new PopupWindow(popupViewLayout, ViewGroup.LayoutParams.MATCH_PARENT, mHight, true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//防止挡住系统键
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.common_pop_button_anim);
        popupWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        setDismissListener();
        show = true;
    }

    public void hide() {
        show = false;
        if (popupWindow == null) return;
        popupWindow.dismiss();
        popupWindow = null;
    }

    private void setDismissListener() {
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                show = false;
            }
        }
    );
}
}
