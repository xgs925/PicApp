package com.xcommon.view.layer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by Photostsrs on 2016/6/15.
 */
public class TextLayerView extends LayerView {
    private String TAG = "TextLayerView";
    private boolean needOutline = false;
    private int mOutlineColor = Color.WHITE;
    private int mBackgroundColor = Color.TRANSPARENT;
    private int MAX_SIZE = 500;//px
    private TJOutlineTextView textView;
    String text = "";
    int textSize = 80;//px
    int textColor = Color.WHITE;
    int textGravity = Gravity.START;
    Typeface textTypeface = null;
    boolean existFont = true;
    String needFontName = "";

    private Bitmap textBitmap;
    private TextPaint textPaint;

    public void setText(String text) {
        this.text = text;
        ajustLP();
        updateView();
    }

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
        updateView();
    }

    public void setTextTypeface(Typeface typeface) {
        textTypeface = typeface;
        ajustLPByTypeface(typeface);
        updateView();
    }

    public void setNeedFontName(String needFontName) {
        this.needFontName = needFontName;
    }

    public String getNeedFontName() {
        return needFontName;
    }

    public boolean isNeedOutline() {
        return needOutline;
    }

    public void setNeedOutline(boolean needOutline) {
        this.needOutline = needOutline;
        textView.setNeedOutline(needOutline);
        updateView();
    }

    public void setExistFont(boolean existFont) {
        this.existFont = existFont;
    }

    public boolean isExistFont() {
        return existFont;
    }

    @Override
    public Bitmap getBitmap() {
        textBitmap = newTextBitmap();
        return textBitmap;
    }

    public Bitmap getCacheBitmap() {
        if (textBitmap == null)
            textBitmap = newTextBitmap();
        return textBitmap;
    }

    public TextView getTextView() {
        return textView;
    }

    public String getText() {
        return text;
    }

    public int getTextSize() {
        return textSize;
    }

    public TextLayerView(Context context, TextView textView) {
        super(context);
        this.text = textView.getText().toString();
        this.textColor = textView.getCurrentTextColor();
        this.textGravity = textView.getGravity();
        this.textTypeface = textView.getTypeface();
        this.textSize = (int) textView.getTextSize();
        initTextLayer(context);
    }

    public TextLayerView(Context context, String text) {
        super(context);
        this.text = text;
        initTextLayer(context);
    }

    public TextLayerView(Context context) {
        super(context);
        initTextLayer(context);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        textBitmap = bitmap;
    }

    private void initTextLayer(Context context) {
        textPaint = new TextPaint();
        textView = new TJOutlineTextView(context);
        textView.setNeedOutline(needOutline);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(lp);
        imageLayout.addView(textView);
        visTextView();
        ajustLP();
        updateView();
    }

    public void updateView() {
        updateTextView();
//        updateImageView();
    }

    private void updateTextView() {
        textView.setText(text);
        textView.setGravity(textGravity);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setTextColor(textColor);
        textView.setBackgroundColor(mBackgroundColor);
        textView.setOutlineColor(mOutlineColor);
        if (textTypeface != null) textView.setTypeface(textTypeface);
    }

    public void updateImageView() {
        setImageBitmap(newTextBitmap());
    }

    @Override
    public void setLayerAlpha(int layerAlpha) {
        super.setLayerAlpha(layerAlpha);
        textView.setAlpha((float) (layerAlpha * 1.0 / 255));
    }

    public void visTextView() {
        imageView.setVisibility(GONE);
        textView.setVisibility(VISIBLE);
    }

    public void visImageView() {
        imageView.setVisibility(VISIBLE);
        textView.setVisibility(GONE);
    }

    private Bitmap newTextBitmap() {
//        text = textView.getText().toString();
//        textColor = textView.getCurrentTextColor();
        textSize = (int) textView.getTextSize();
//        textTypeface = textView.getTypeface();
//        textGravity = textView.getGravity();
        textPaint.setTextSize(textSize * mScale);
        textPaint.setColor(textColor);
        textPaint.setTypeface(textTypeface);
        textPaint.setDither(true); // 获取跟清晰的图像采样
        textPaint.setFilterBitmap(true);// 过滤
        textPaint.setAntiAlias(true);
        String[] texts = text.split("\\n");
        int width = 0;
        for (int i = 0; i < texts.length; i++) {
            int temWidth = (int) Math.ceil(textPaint.measureText(texts[i]));
            if (temWidth > width) width = temWidth;
        }

        Layout.Alignment alignment = null;
        switch (textGravity) {
            case Gravity.START | 51:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
            case Gravity.CENTER:
                alignment = Layout.Alignment.ALIGN_CENTER;
                break;
            case Gravity.END | 53:
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
        }

        StaticLayout layout = new StaticLayout(text, textPaint, width, alignment, 1.0F, 0.0F, true);
        int height = layout.getHeight();
        Bitmap layerBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(layerBitmap);// 初始化画布
        if (mBackgroundColor != Color.TRANSPARENT) {
            canvas.drawColor(mBackgroundColor);
        }
        if (needOutline) {
            textPaint.setColor(mOutlineColor);
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setStrokeWidth(textPaint.getTextSize() / 12f);// 描边宽度
            layout.draw(canvas);
        }
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(0);
        textPaint.setColor(textColor);
        layout.draw(canvas);
        return layerBitmap;
    }

    @Override
    protected float ajustScale(float scale) {
        if (scale * textSize > MAX_SIZE) return MAX_SIZE * 1.0f / textSize;
        return super.ajustScale(scale);
    }


    public void ajustLPByTypeface(Typeface tf) {
        textPaint.setTypeface(tf);
        ajustLP();
    }

    private void ajustLP() {
        textPaint.setTextSize(textSize);
        int width = 0;
        String[] texts = text.split("\\n");
        for (int i = 0; i < texts.length; i++) {
            int temWidth = (int) Math.ceil(textPaint.measureText(texts[i]));
            if (temWidth > width) width = temWidth;
        }
        StaticLayout layout = new StaticLayout(text, textPaint, width,
                null, 1.0F, 0.0F, true);
        int height = layout.getHeight();

        ViewGroup.LayoutParams lp = getLayoutParams();

        if (lp == null) {
            lp = new ViewGroup.LayoutParams(width, height);
            setLayoutParams(lp);
        } else {
            lp.width = width;
            lp.height = height;
        }
        offsetTo(offsetPoint);
    }

    public void setTextColor(int color) {
        textColor = color;
        visTextView();
        updateTextView();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
        visTextView();
        updateTextView();
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setOutlineColor(int color) {
        mOutlineColor = color;
        if (color == Color.TRANSPARENT) {
            needOutline = false;
        } else {
            needOutline = true;
        }
        visTextView();
        updateTextView();
    }

    public int getOutlineColor() {
        return mOutlineColor;
    }
}
