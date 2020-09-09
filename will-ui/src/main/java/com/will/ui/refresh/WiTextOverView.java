package com.will.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.will.ui.R;

public class WiTextOverView extends WiOverView {
    ImageView mImageView;
    TextView mTextView;
    public WiTextOverView(@NonNull Context context) {
        super(context);
    }

    public WiTextOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WiTextOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.wi_text_over_view, this, true);
        mImageView = root.findViewById(R.id.iv_rotate);
        mTextView = root.findViewById(R.id.tv_refresh_text);
    }

    @Override
    protected void onScroll(int scrollY, int pullRefreshHeight) {

    }

    @Override
    protected void onVisible() {
        mTextView.setText("下拉刷新");
    }

    @Override
    public void onOver() {
        mTextView.setText("松开刷新");
    }

    @Override
    public void onRefresh() {
        mTextView.setText("正在刷新");
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        mImageView.setAnimation(animation);
    }

    @Override
    void onFinish() {
        mImageView.clearAnimation();
    }
}
