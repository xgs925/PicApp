package com.xcommon.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xcommon.R;
import com.xcommon.utils.ImageLoaderUtil;

/**
 * Created by Photostsrs on 2016/10/27.
 */
public class TJHelpView {
    private Activity activity;
    private int[] bitmapIds;
    private String name;

    public TJHelpView(Activity activity, int[] bitmapIds, String name) {
        this.activity = activity;
        this.bitmapIds = bitmapIds;
        this.name = name;
        init();
    }


    private View init() {
        View popupView = activity.getLayoutInflater().inflate(R.layout.view_help, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//防止挡住系统键
        popupWindow.setAnimationStyle(R.style.help_pop_anim);
        popupWindow.showAtLocation((activity.findViewById(android.R.id.content)), Gravity.FILL, 0, 0);

        ViewPager viewPager = (ViewPager) popupView.findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getCount() {
                return bitmapIds.length;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(activity);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageLoader.getInstance().displayImage("drawable://" + bitmapIds[position], new ImageViewAware(imageView), ImageLoaderUtil.getMemOptions());
                container.addView(imageView);
                return imageView;
            }
        });
        TextView nameTextView = (TextView) popupView.findViewById(R.id.name);
        nameTextView.setText(name);
        View back = popupView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        View control = popupView.findViewById(R.id.control);
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        return popupView;
    }
}
