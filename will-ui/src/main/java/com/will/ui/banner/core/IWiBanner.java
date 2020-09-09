package com.will.ui.banner.core;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.will.ui.banner.indicator.IWiIndicator;

import java.util.List;

public interface IWiBanner {
    void setBannerData(@LayoutRes int layoutResId, @NonNull List<? extends WiBannerMo> models);

    void setBannerData(@NonNull List<? extends WiBannerMo> models);

    void setWiIndicator(IWiIndicator<?> indicator);

    void setAutoPlay(boolean autoPlay);

    void setLoop(boolean loop);

    void setIntervalTime(int intervalTime);

    void setBindAdapter(IWiBindAdapter bindAdapter);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    void setOnBannerClickListener(OnBannerClickListener onBannerClickListener);

    void setScrollDuration(int duration);

    interface OnBannerClickListener {
        void onBannerClick(@NonNull WiBannerAdapter.WiBannerViewHolder viewHolder, @NonNull WiBannerMo bannerMo, int position);
    }
}
