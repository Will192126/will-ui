package com.will.ui.refresh;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.will.library.log.HiLog;

public class WiScrollerUtil {
    /**
     * 判断view是否发生了滚动
     *
     * @param view 需判断的view
     * @return 发生了滚动返回true，否则返回false
     */
    public static boolean isScrolled(@NonNull View view) {
        if (view instanceof AdapterView) {
            AdapterView adapterView = (AdapterView) view;
            if (adapterView.getFirstVisiblePosition() != 0
                    || adapterView.getFirstVisiblePosition() == 0
                    && adapterView.getChildAt(0) != null
                    && adapterView.getChildAt(0).getTop() < 0) {
                return true;
            }
        } else if (view.getScrollY() > 0){
            return true;
        }

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            View item = recyclerView.getChildAt(0);
            int firstPosition = recyclerView.getChildAdapterPosition(item);
            HiLog.d("----:top", view.getTop());
            return firstPosition != 0 || view.getTop() != 0;
        }
        return false;
    }
    /**
     * 查找可滚动的子View，如果对应的下标不是可滚动的view，往下再找一层，如果还不是，则返回传入下标对应的view。
     * @param viewGroup 父容器
     * @param index 子View下标
     * @return 可滚动的子view，否则返回传入下标对应的子view
     */
    public static View findScrollableChild(@NonNull ViewGroup viewGroup, int index) {
        View child = viewGroup.getChildAt(index);
        if (child instanceof RecyclerView || child instanceof AdapterView || child instanceof ScrollView) {
            return child;
        }
        // 往下再找一层
        if (child instanceof ViewGroup) {
            View tempChild = ((ViewGroup) child).getChildAt(0);
            if (tempChild instanceof RecyclerView || tempChild instanceof AdapterView || tempChild instanceof ScrollView) {
                child = tempChild;
            }
        }
        return child;
    }
}
