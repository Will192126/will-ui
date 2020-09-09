package com.will.ui.item

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class WiDataItem<DATA, VH : RecyclerView.ViewHolder>(data: DATA) {
    var mData: DATA? = null

    init {
        mData = data
    }

    abstract fun onBindData(holder: RecyclerView.ViewHolder, position: Int)

    /**
     * 获取item的布局资源id
     */
    open fun getItemLayoutRes(): Int {
        return -1
    }

    /**
     * 获取item的视图view
     */
    open fun getItemView(parent: ViewGroup): View? {
        return null
    }

    /**
     * 刷新列表
     */
    fun refreshItem() {

    }

    /**
     * 从列表上移除
     */
    fun removeItem() {

    }
}