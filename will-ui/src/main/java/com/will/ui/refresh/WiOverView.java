package com.will.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.will.library.util.WiDisplayUtil;

public abstract class WiOverView extends FrameLayout {
    enum RefreshState {
        /**
         * 初始态
         */
        STATE_INIT,
        /**
         * Header完全展示
         */
        STATE_VISIBLE,
        /**
         * 超出可刷新距离
         */
        STATE_OVER,
        /**
         * 刷新中
         */
        STATE_REFRESH,
        /**
         * 超出刷新位置并松开手
         */
        STATE_OVER_RELEASE
    }

    protected RefreshState mRefreshState = RefreshState.STATE_INIT;
    /**
     * 触发下拉刷新的最小高度
     */
    public int pullRefreshHeight;
    /**
     * 最小阻尼
     */
    public float minDamp = 1.6f;
    /**
     * 最大阻尼
     */
    public float maxDamp = 2.2f;

    public WiOverView(@NonNull Context context) {
        this(context, null, 0);
    }

    public WiOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WiOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    protected void preInit() {
        pullRefreshHeight = WiDisplayUtil.dp2px(66, getResources());
        init();
    }

    public abstract void init();

    protected abstract void onScroll(int scrollY, int pullRefreshHeight);

    /**
     * Overlay可见
     */
    protected abstract void onVisible();

    /**
     * 下拉高度超过Overlay高度
     */
    public abstract void onOver();

    /**
     * 刷新
     */
    public abstract void onRefresh();

    /**
     * 刷新完成
     */
    abstract void onFinish();

    void setState(RefreshState refreshState) {
        mRefreshState = refreshState;
    }

    RefreshState getState() {
        return mRefreshState;
    }
}
