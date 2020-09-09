package com.will.ui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.will.library.util.WiDisplayUtil;
import com.will.ui.tab.bottom.WiTabBottom;
import com.will.ui.tab.bottom.WiTabBottomInfo;
import com.will.ui.tab.common.IHiTab;
import com.will.ui.tab.common.IHiTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WiTabTopLayout extends HorizontalScrollView implements IHiTabLayout<WiTabTop, WiTabTopInfo<?>> {
    private List<OnTabSelectedListener<WiTabTopInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private WiTabTopInfo<?> mSelectedInfo;
    private List<WiTabTopInfo<?>> mInfoList;
    private int tabWidth;

    public WiTabTopLayout(Context context) {
        this(context, null, 0);
    }

    public WiTabTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WiTabTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public WiTabTop findTab(@NonNull WiTabTopInfo<?> data) {
        ViewGroup ll = getRootLayout(false);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof WiTabTop) {
                WiTabTop tabTop = (WiTabTop) child;
                if (tabTop.getTabInfo() == data) {
                    return tabTop;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<WiTabTopInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull WiTabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<WiTabTopInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.mInfoList = infoList;

        // 移除之前已添加的View
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }
        Iterator<OnTabSelectedListener<WiTabTopInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof WiTabTop) {
                iterator.remove();
            }
        }
        mSelectedInfo = null;
        LinearLayout linearLayout = getRootLayout(true);
        for (int i = 0; i < infoList.size(); i++) {
            final WiTabTopInfo info = infoList.get(i);
            WiTabTop tabTop = new WiTabTop(getContext());
            tabSelectedChangeListeners.add(tabTop);
            tabTop.setHiTabInfo(info);
            linearLayout.addView(tabTop);
            tabTop.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
        }
    }

    private void onSelected(@NonNull WiTabTopInfo<?> nextInfo) {
        for (OnTabSelectedListener<WiTabTopInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(mInfoList.indexOf(nextInfo), mSelectedInfo, nextInfo);
        }
        this.mSelectedInfo = nextInfo;

        autoScroll(nextInfo);
    }

    private void autoScroll(WiTabTopInfo<?> nextInfo) {
        WiTabTop tabTop = findTab(nextInfo);
        if (null == tabTop) {
            return;
        }
        int index = mInfoList.indexOf(nextInfo);
        int[] loc = new int[2];
        tabTop.getLocationInWindow(loc);

        if (0 == tabWidth) {
            tabWidth = tabTop.getWidth();
        }

        int scrollDistance;
        // 判断滚动方向
        if ((loc[0] + tabWidth / 2) > WiDisplayUtil.getDisplayWidthInPx(getContext()) / 2) {
            scrollDistance = getScrollDistance(index, 2);
        } else {
            scrollDistance = getScrollDistance(index, -2);
        }
        scrollTo(getScrollX() + scrollDistance, 0);
    }

    /**
     * 获取滚动距离
     *
     * @param index
     * @param step
     * @return
     */
    private int getScrollDistance(int index, int step) {
        int distance = 0;
        for (int i = 0; i <= Math.abs(step); i++) {
            int next;
            if (step < 0) {
                next = step + i + index;
            } else {
                next = step - i + index;
            }

            if (next >= 0 && next < mInfoList.size()) {
                if (step < 0) {
                    distance -= getDistance(next, false);
                } else {
                    distance += getDistance(next, true);
                }
            }
        }
        return distance;
    }

    private int getDistance(int index, boolean toRight) {
        WiTabTop tabTop = findTab(mInfoList.get(index));
        if (null == tabTop) {
            return 0;
        }

        Rect rect = new Rect();
        tabTop.getLocalVisibleRect(rect);
        if (toRight) {
            if (rect.right > tabWidth) { // 完全没有展示
                return tabWidth;
            } else { // 部分展示
                return tabWidth - rect.right;
            }
        } else {
            if (tabWidth <= -rect.left) { // 完全没有展示
                return tabWidth;
            } else if (rect.left > 0){ // 部分展示
                return rect.left;
            }
        }
        return 0;
    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if (rootView == null) {
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            addView(rootView, params);
        } else if (clear) {
            rootView.removeAllViews();
        }

        return rootView;
    }
}
