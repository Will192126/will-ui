package com.will.ui.banner.indicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.will.library.util.WiDisplayUtil;
import com.will.ui.R;

public class WiCircleIndicator extends FrameLayout implements IWiIndicator<FrameLayout> {
    private static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 正常状态下的点
     */
    @DrawableRes
    private int mPointNormal = R.drawable.shape_point_normal;
    /**
     * 选中状态下的点
     */
    @DrawableRes
    private int mPointSelected = R.drawable.shape_point_select;
    /**
     * 指示点水平方向的间距
     */
    private int mPointHorizontalPadding;
    /**
     * 指示点垂直方向的间距
     */
    private int mPointVerticalPadding;
    public WiCircleIndicator(@NonNull Context context) {
        this(context, null);
    }

    public WiCircleIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WiCircleIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPointHorizontalPadding = WiDisplayUtil.dp2px(5, getResources());
        mPointVerticalPadding = WiDisplayUtil.dp2px(15, getResources());
    }

    @Override
    public FrameLayout get() {
        return this;
    }

    @Override
    public void onInflate(int count) {
        removeAllViews();
        if (count <= 0) {
            return;
        }
        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);

        ImageView imageView;
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        imageViewParams.gravity = Gravity.CENTER_VERTICAL;
        imageViewParams.setMargins(mPointHorizontalPadding, mPointVerticalPadding, mPointHorizontalPadding, mPointVerticalPadding);
        for (int i = 0; i < count; i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(imageViewParams);
            if (i == 0) {
                imageView.setImageResource(mPointSelected);
            } else {
                imageView.setImageResource(mPointNormal);
            }
            container.addView(imageView);
        }
        LayoutParams viewGroupParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        viewGroupParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        addView(container, viewGroupParams);
    }

    @Override
    public void onIndicatorChange(int current, int count) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ImageView imageView = (ImageView) viewGroup.getChildAt(i);
            if (i == current) {
                imageView.setImageResource(mPointSelected);
            } else {
                imageView.setImageResource(mPointNormal);
            }
            imageView.requestLayout();
        }
    }
}
