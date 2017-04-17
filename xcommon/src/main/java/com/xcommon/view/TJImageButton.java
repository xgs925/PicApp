package com.xcommon.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Photostsrs on 2017/3/13.
 */

public class TJImageButton extends ImageView {
    public TJImageButton(Context context) {
        super(context);
    }


    public TJImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TJImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TJImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isClickable()) return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setColorFilter(0x99000000);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.setColorFilter(null);
                break;
        }
        return super.onTouchEvent(event);
    }
}
