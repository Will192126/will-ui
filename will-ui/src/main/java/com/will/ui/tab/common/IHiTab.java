package com.will.ui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

public interface IHiTab<D> extends IHiTabLayout.OnTabSelectedListener<D> {
    void setHiTabInfo(@NonNull D data);

    /**
     * 动态修改item高度
     *
     * @param height item高度
     */
    void resetHeight(@Px int height);
}
