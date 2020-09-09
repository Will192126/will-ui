package com.will.ui.banner.core;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.will.ui.R;
import com.will.ui.banner.WiBanner;
import com.will.ui.banner.indicator.WiCircleIndicator;
import com.will.ui.banner.indicator.IWiIndicator;

import java.util.List;

/**
 * WiBanner控制器，辅助WiBanner完成各种功能的控制
 * 将WiBanner的一些逻辑内聚在这里，保证暴露给使用者的WiBanner干净整洁
 */
public class WiBannerDelegate implements IWiBanner, ViewPager.OnPageChangeListener {
    private Context mContext;
    private WiBanner mBanner;
    private WiBannerAdapter mAdapter;
    private IWiIndicator<?> mIndicator;
    private boolean mIsAutoPlay;
    private boolean mIsLoop;
    private List<? extends WiBannerMo> mModels;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mIntervalTime = 5000;
    private IWiBanner.OnBannerClickListener mOnBannerClickListener;
    private WiViewPager mViewPager;

    public WiBannerDelegate(Context context, WiBanner banner) {
        mContext = context;
        mBanner = banner;
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends WiBannerMo> models) {
        mModels = models;
        init(layoutResId);
    }

    @Override
    public void setBannerData(@NonNull List<? extends WiBannerMo> models) {
        setBannerData(R.layout.wi_banner_item_image, models);
    }

    @Override
    public void setWiIndicator(IWiIndicator<?> indicator) {
        mIndicator = indicator;
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        mIsAutoPlay = autoPlay;
        if (mAdapter != null) {
            mAdapter.setAutoPlay(autoPlay);
        }
        if (mViewPager != null) {
            mViewPager.setAutoPlay(autoPlay);
        }
    }

    @Override
    public void setLoop(boolean loop) {
        mIsLoop = loop;
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            mIntervalTime = intervalTime;
        }
    }

    @Override
    public void setBindAdapter(IWiBindAdapter bindAdapter) {
        mAdapter.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        mOnBannerClickListener = onBannerClickListener;
    }

    @Override
    public void setScrollDuration(int duration) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null && mAdapter.getRealCount() != 0) {
            mOnPageChangeListener.onPageScrolled(position % mAdapter.getRealCount(), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter.getRealCount() == 0) {
            return;
        }
        position = position % mAdapter.getRealCount();
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        if (mIndicator != null) {
            mIndicator.onIndicatorChange(position, mAdapter.getRealCount());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void init(@LayoutRes int layoutResId) {
        if (mAdapter == null) {
            mAdapter = new WiBannerAdapter(mContext);
        }
        if (mIndicator == null) {
            mIndicator = new WiCircleIndicator(mContext);
        }

        mIndicator.onInflate(mModels.size());
        mAdapter.setLayoutResId(layoutResId);
        mAdapter.setBannerData(mModels);
        mAdapter.setAutoPlay(mIsAutoPlay);
        mAdapter.setLoop(mIsLoop);
        mAdapter.setOnBannerClickListener(mOnBannerClickListener);

        mViewPager = new WiViewPager(mContext);
        mViewPager.setIntervalTime(mIntervalTime);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAutoPlay(mIsAutoPlay);
        mViewPager.setAdapter(mAdapter);

        if ((mIsLoop || mIsAutoPlay) && mAdapter.getRealCount() != 0) {
            int firstItemPosition = mAdapter.getFirstItemPosition();
            mViewPager.setCurrentItem(firstItemPosition);
        }

        mBanner.removeAllViews();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mBanner.addView(mViewPager, params);
        mBanner.addView(mIndicator.get(), params);

    }

}
