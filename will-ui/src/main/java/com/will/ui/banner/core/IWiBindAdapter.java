package com.will.ui.banner.core;

import androidx.annotation.NonNull;

/**
 * WiBanner数据绑定接口。
 * 通过实现该接口可以实现数据绑定与框架层解耦
 */
public interface IWiBindAdapter {
    void onBind(WiBannerAdapter.WiBannerViewHolder viewHolder, WiBannerMo mo, int position);
}
