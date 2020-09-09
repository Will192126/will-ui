package com.will.ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.will.ui.R;
import com.will.ui.banner.core.IWiBanner;
import com.will.ui.banner.core.IWiBindAdapter;
import com.will.ui.banner.core.WiBannerDelegate;
import com.will.ui.banner.core.WiBannerMo;
import com.will.ui.banner.indicator.IWiIndicator;

import java.util.List;

public class WiBanner extends FrameLayout implements IWiBanner {
    WiBannerDelegate mDelegate;
    public WiBanner(@NonNull Context context) {
        this(context, null);
    }

    public WiBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WiBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @NonNull AttributeSet attrs) {
        mDelegate = new WiBannerDelegate(context, this);
        initCustomAttrs(context, attrs);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WiBanner);
        boolean autoPlay = typedArray.getBoolean(R.styleable.WiBanner_auto_play, true);
        boolean loop = typedArray.getBoolean(R.styleable.WiBanner_loop, true);
        int intervalTime = typedArray.getInteger(R.styleable.WiBanner_interval_time, -1);
        setAutoPlay(autoPlay);
        setLoop(loop);
        setIntervalTime(intervalTime);
        typedArray.recycle();
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends WiBannerMo> models) {
        mDelegate.setBannerData(layoutResId, models);
    }

    @Override
    public void setBannerData(@NonNull List<? extends WiBannerMo> models) {
        mDelegate.setBannerData(models);
    }

    @Override
    public void setWiIndicator(IWiIndicator<?> indicator) {
        mDelegate.setWiIndicator(indicator);
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        mDelegate.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        mDelegate.setLoop(loop);
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        mDelegate.setIntervalTime(intervalTime);
    }

    @Override
    public void setBindAdapter(IWiBindAdapter bindAdapter) {
        mDelegate.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mDelegate.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        mDelegate.setOnBannerClickListener(onBannerClickListener);
    }

    @Override
    public void setScrollDuration(int duration) {
        mDelegate.setScrollDuration(duration);
    }
}
