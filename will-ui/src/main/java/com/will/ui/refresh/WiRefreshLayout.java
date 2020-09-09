package com.will.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WiRefreshLayout extends FrameLayout implements IWiRefresh {
    private WiOverView.RefreshState mState;
    private WiOverView mOverView;
    private GestureDetector mGestureDetector;
    private IWiRefresh.RefreshListener mRefreshListener;
    private AutoScroller mAutoScroller;
    private int mLastY;
    private boolean mIsScrollDisabled;

    /**
     * 根据偏移量移动header和child
     *
     * @param offsetY 垂直偏移量
     * @param isAutoScroll 是否自动滚动
     * @return
     */
    private boolean moveDown(int offsetY, boolean isAutoScroll) {
        View header = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;
        if (childTop <= 0) {
            offsetY = -child.getTop();
            // 移动header和child到原始位置
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mState != WiOverView.RefreshState.STATE_REFRESH) {
                mState = WiOverView.RefreshState.STATE_INIT;
            }
        } else if (mState == WiOverView.RefreshState.STATE_REFRESH && childTop > mOverView.pullRefreshHeight) {
            // 正在下拉刷新中，禁止下拉
            return false;
        } else if (childTop <= mOverView.pullRefreshHeight) {
            // 还没超出指定的刷新距离
            if (mOverView.getState() != WiOverView.RefreshState.STATE_VISIBLE && !isAutoScroll) {
                mOverView.onVisible();
                mOverView.setState(WiOverView.RefreshState.STATE_VISIBLE);
                mState = WiOverView.RefreshState.STATE_VISIBLE;
            }
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == mOverView.pullRefreshHeight && mState == WiOverView.RefreshState.STATE_OVER_RELEASE) {
                // 下拉刷新完成
                refresh();
            }
        } else {
            if (mOverView.getState() != WiOverView.RefreshState.STATE_OVER && !isAutoScroll) {
                // 超出刷新位置
                mOverView.onOver();
                mOverView.setState(WiOverView.RefreshState.STATE_OVER);
            }
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (mOverView != null) {
            mOverView.onScroll(header.getBottom(), mOverView.pullRefreshHeight);
        }
        return true;
    }

    private void refresh() {
        if (mRefreshListener != null) {
            mState = WiOverView.RefreshState.STATE_REFRESH;
            mOverView.onRefresh();
            mOverView.setState(WiOverView.RefreshState.STATE_REFRESH);
            mRefreshListener.onRefresh();
        }
    }

    public WiRefreshLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public WiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), mDetector);
        mAutoScroller = new AutoScroller();
    }

    @Override
    public void setDisableRefreshScroll(boolean isDisabled) {
        mIsScrollDisabled = isDisabled;
    }

    @Override
    public void refreshFinished() {
        View header = getChildAt(0);
        mOverView.onFinish();
        mOverView.setState(WiOverView.RefreshState.STATE_INIT);
        int bottom = header.getBottom();
        if (bottom > 0) {
            recover(bottom);
        }
        mState = WiOverView.RefreshState.STATE_INIT;
    }

    @Override
    public void setRefreshOverView(WiOverView overView) {
        if (mOverView != null) {
            removeView(mOverView);
        }
        mOverView = overView;
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(overView, 0, params);
    }

    @Override
    public void setRefreshListener(RefreshListener listener) {
        mRefreshListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mAutoScroller.isFinished()) {
            return false;
        }

        View header = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            if (header.getBottom() > 0) {
                if (mState != WiOverView.RefreshState.STATE_REFRESH) { //非正在刷新状态
                    recover(header.getBottom());
                    return false;
                }
            }
            mLastY = 0;
        }
        boolean consumed = mGestureDetector.onTouchEvent(ev);
        if ((consumed || (mState != WiOverView.RefreshState.STATE_INIT
                            && mState != WiOverView.RefreshState.STATE_REFRESH))
                && header.getBottom() != 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL); // 让父类接收不到真实事件
            return super.dispatchTouchEvent(ev);
        }

        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    private void recover(int distance) {
        if (mRefreshListener != null && distance > mOverView.pullRefreshHeight) {
            mAutoScroller.recover(distance - mOverView.pullRefreshHeight);
            mState = WiOverView.RefreshState.STATE_OVER_RELEASE;
        } else {
            mAutoScroller.recover(distance);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View header = getChildAt(0);
        View child = getChildAt(1);
        if (header != null && child != null) {
            int childTop = child.getTop();
            if (mState == WiOverView.RefreshState.STATE_REFRESH) {
                header.layout(0, mOverView.pullRefreshHeight - header.getMeasuredHeight(), right, mOverView.pullRefreshHeight);
                child.layout(0, mOverView.pullRefreshHeight, right, mOverView.pullRefreshHeight + child.getMeasuredHeight());
            } else {
                header.layout(0, childTop - header.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, childTop + child.getMeasuredHeight());
            }

            View other;
            for (int i = 2; i < getChildCount(); i++) {
                other = getChildAt(i);
                other.layout(0, top, right, bottom);
            }
        }
    }

    WiGestureDetector mDetector = new WiGestureDetector() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY)
                    || mRefreshListener != null
                    && !mRefreshListener.enableRefresh()) {
                // 横向滑动，或刷新被禁止
                return false;
            }
            if (mIsScrollDisabled && mState == WiOverView.RefreshState.STATE_REFRESH) {
                // 刷新时禁止滑动
                return true;
            }

            View header = getChildAt(0);
            View child = WiScrollerUtil.findScrollableChild(WiRefreshLayout.this, 1);
            if (WiScrollerUtil.isScrolled(child)) {
                // child发生了滚动，不处理
                return false;
            }
            if ((mState != WiOverView.RefreshState.STATE_REFRESH || header.getBottom() <= mOverView.pullRefreshHeight)
                    && (header.getBottom() > 0 || distanceY < 0.0f)) {
                if (mState != WiOverView.RefreshState.STATE_OVER_RELEASE) { // 还在滑动中
                    int speed;
                    if (child.getTop() < mOverView.pullRefreshHeight) {
                        speed = (int) (mLastY / mOverView.minDamp);
                    } else {
                        speed = (int) (mLastY / mOverView.maxDamp);
                    }
                    boolean bool = moveDown(speed, false);
                    mLastY = (int) -distanceY;
                    return bool;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    /**
     * 借助Scroller实现视图的自动滚动
     * https://juejin.im/post/5c7f4f0351882562ed516ab6
     */
    private class AutoScroller implements Runnable {
        private Scroller mScroller;
        private int mLastY;
        private boolean mIsFinished;

        public AutoScroller() {
            mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) { // 还未滚动完
                moveDown(mLastY - mScroller.getCurrY(), true);
                mLastY = mScroller.getCurrY();
                post(this);
            } else { // 滚动完
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        void recover(int distance) {
            if (distance <= 0) {
                return;
            }
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, distance, 300);
            post(this);
        }

        boolean isFinished() {
            return mIsFinished;
        }
    }
}
