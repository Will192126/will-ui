package com.will.ui.banner.indicator;

import android.view.View;

/**
 * 指示器接口
 *
 * @param <T> 自定义指示器view
 */
public interface IWiIndicator<T extends View> {
    T get();

    /**
     * 初始化指示器
     * @param count 指示器个数
     */
    void onInflate(int count);

    /**
     * 指示器切换回调函数
     * @param current 当前指示器
     * @param count 指示器个数
     */
    void onIndicatorChange(int current, int count);
}
