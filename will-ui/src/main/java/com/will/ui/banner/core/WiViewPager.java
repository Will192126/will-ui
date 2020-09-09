package com.will.ui.banner.core;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class WiViewPager extends ViewPager {
    private int mIntervalTime;
    private boolean mIsAutoPlay = true;
    private boolean mIsLayout;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // 切换到下一个
            next();
            mHandler.postDelayed(this, mIntervalTime);
        }
    };

    public WiViewPager(@NonNull Context context) {
        super(context);
    }

    public WiViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mIsLayout && getAdapter() != null && getAdapter().getCount() > 0) {
            try {
                // 解决RecyclerView + ViewPager混用时自动播放的bug
                Field mScroller = ViewPager.class.getDeclaredField("mFirstLayout");
                mScroller.setAccessible(true);
                mScroller.set(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        // 解决RecyclerView + ViewPager混用时自动播放的bug
        if (((Activity) getContext()).isFinishing()) {
            super.onDetachedFromWindow();
        }
        stop();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mIsLayout = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                start();
                break;
            default:
                stop();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setIntervalTime(int intervalTime) {
        mIntervalTime = intervalTime;
    }

    public void setAutoPlay(boolean isAutoPlay) {
        mIsAutoPlay = isAutoPlay;
        if (!mIsAutoPlay) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void start() {
        mHandler.removeCallbacksAndMessages(null);
        if (mIsAutoPlay) {
            mHandler.postDelayed(mRunnable, mIntervalTime);
        }
    }

    public void stop() {
        mHandler.removeCallbacksAndMessages(null);
    }


    private int next() {
        int nextPosition = -1;
        if (getAdapter() == null || getAdapter().getCount() <= 1) {
            stop();
            return nextPosition;
        }

        nextPosition = getCurrentItem() + 1;

        // 下一个索引大于adapter的最大数量时重新开始
        if (nextPosition >= getAdapter().getCount()) {
            // 获取第一个item的索引
            nextPosition = ((WiBannerAdapter) getAdapter()).getFirstItemPosition();
        }
        setCurrentItem(nextPosition, true);
        return nextPosition;
    }
}
