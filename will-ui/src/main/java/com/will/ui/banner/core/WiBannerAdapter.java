package com.will.ui.banner.core;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class WiBannerAdapter extends PagerAdapter {
    private Context mContext;
    private SparseArray<WiBannerViewHolder> mCachedViews = new SparseArray<>();
    private IWiBanner.OnBannerClickListener mOnClickListener;
    private IWiBindAdapter mBindAdapter;
    private List<? extends WiBannerMo> mModels;
    /**
     * 是否自动轮播标识
     */
    private boolean mIsAutoPlay = true;
    /**
     * 非自动轮播状态下是否能够循环切换
     */
    private boolean mIsLoop = false;
    private int mLayoutResId = -1;

    public WiBannerAdapter(Context context) {
        mContext = context;
    }

    public void setBannerData(@NonNull List<? extends WiBannerMo> models) {
        mModels = models;
        initCachedView();
        notifyDataSetChanged();
    }

    public void setBindAdapter(IWiBindAdapter bindAdapter) {
        mBindAdapter = bindAdapter;
    }

    public void setOnBannerClickListener(IWiBanner.OnBannerClickListener listener) {
        mOnClickListener = listener;
    }

    public void setLayoutResId(@LayoutRes int layoutResId) {
        mLayoutResId = layoutResId;
    }

    public void setAutoPlay(boolean isAutoPlay) {
        mIsAutoPlay = isAutoPlay;
    }

    public void setLoop(boolean isLoop) {
        mIsLoop = isLoop;
    }

    @Override
    public int getCount() {
        return mIsAutoPlay ? Integer.MAX_VALUE : (mIsLoop ? Integer.MAX_VALUE : getRealCount());
    }

    public int getRealCount() {
        return mModels == null ? 0 : mModels.size();
    }

    public int getFirstItemPosition() {
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position;
        if (getRealCount() > 0) {
            realPosition = position % getRealCount();
        }
        WiBannerViewHolder viewHolder = mCachedViews.get(realPosition);
        onBind(viewHolder, mModels.get(realPosition), realPosition);
        if (viewHolder.rootView.getParent() != null) {
            ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
        }
        container.addView(viewHolder.rootView);
        return viewHolder.rootView;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // 让item每次都会刷新
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 不调用父类方法，view从container被移除的时候不会被销毁,避免出现白屏
    }

    protected void onBind(@NonNull final WiBannerViewHolder viewHolder, @NonNull final WiBannerMo mo, final int position) {
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onBannerClick(viewHolder, mo, position);
                }
            }
        });

        if (mBindAdapter != null) {
            mBindAdapter.onBind(viewHolder, mo, position);
        }
    }

    private void initCachedView() {
        mCachedViews = new SparseArray<>();
        for (int i = 0; i < mModels.size(); i++) {
            WiBannerViewHolder viewHolder = new WiBannerViewHolder(createView(LayoutInflater.from(mContext), null));
            mCachedViews.put(i, viewHolder);
        }
    }

    private View createView(LayoutInflater inflater, ViewGroup parent) {
        if (-1 == mLayoutResId) {
            throw new IllegalArgumentException("Please call setLayoutResId first.");
        }
        return inflater.inflate(mLayoutResId, parent, false);
    }

    public static class WiBannerViewHolder {
        private SparseArray<View> viewSparseArray;
        View rootView;

        public WiBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public <V extends View> V findViewById(int id) {
            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;
            }
            if (this.viewSparseArray == null) {
                this.viewSparseArray = new SparseArray<>(1);
            }
            V childView = (V) viewSparseArray.get(id);
            if (childView == null) {
                childView = rootView.findViewById(id);
                this.viewSparseArray.put(id, childView);
            }
            return childView;
        }
    }
}
