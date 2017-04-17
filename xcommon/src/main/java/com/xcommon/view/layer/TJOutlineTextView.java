package com.xcommon.view.layer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Photostsrs on 2017/2/17.
 */

public class TJOutlineTextView extends TextView {
    private TextView outlineTextView = null;
    private boolean needOutline = false;

    public void setOutlineColor(int color){
        outlineTextView.setTextColor(color);
    }

    public void setNeedOutline(boolean needOutline) {
        this.needOutline = needOutline;
        invalidate();
    }

    public TJOutlineTextView(Context context) {
        super(context);

        outlineTextView = new TextView(context);
        init();
    }

    public TJOutlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        outlineTextView = new TextView(context, attrs);
        init();
    }

    public TJOutlineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        outlineTextView = new TextView(context, attrs, defStyle);
        init();
    }

    public void init() {
        TextPaint paint = outlineTextView.getPaint();
        paint.setStyle(Paint.Style.STROKE);
        outlineTextView.setTextColor(Color.WHITE);// 描边颜色
        setStrokeWidth();
    }

    private void setStrokeWidth() {
        outlineTextView.getPaint().setStrokeWidth(getTextSize() / 12f);// 描边宽度
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        outlineTextView.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 设置轮廓文字
        CharSequence outlineText = outlineTextView.getText();
        if (outlineText == null || !outlineText.equals(this.getText())) {
            outlineTextView.setGravity(getGravity());
            outlineTextView.setTypeface(getTypeface());
            outlineTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
            outlineTextView.setText(getText());
            postInvalidate();
        }
        outlineTextView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean checkOutlineTextView() {
        if (outlineTextView == null) return true;
        return false;
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
        if (checkOutlineTextView()) return;
        outlineTextView.setGravity(gravity);
    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
        if (checkOutlineTextView()) return;
        outlineTextView.setTypeface(tf);
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        if (checkOutlineTextView()) return;
        outlineTextView.setTextSize(unit, size);
        setStrokeWidth();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (checkOutlineTextView()) return;
        outlineTextView.setText(text, type);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        outlineTextView.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (needOutline)
            outlineTextView.draw(canvas);
        super.onDraw(canvas);
    }
}
