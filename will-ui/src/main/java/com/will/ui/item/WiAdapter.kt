package com.will.ui.item

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType
import java.util.*

class WiAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mContext: Context
    private var mInflater: LayoutInflater? = null
    private var dataSets = ArrayList<WiDataItem<*, RecyclerView.ViewHolder>>()
    private var typeArrays = SparseArray<WiDataItem<*, RecyclerView.ViewHolder>>()

    init {
        mContext = context
        mInflater = LayoutInflater.from(context)
    }

    fun addItem(index: Int, item: WiDataItem<*, RecyclerView.ViewHolder>, notify: Boolean) {
        if (index > 0) {
            dataSets.add(index, item)
        } else {
            dataSets.add(item)
        }

        if (notify) {
            val notifyPosition = if (index > 0) index else dataSets.size - 1
            notifyItemInserted(notifyPosition)
        }
    }

    fun addItems(items: List<WiDataItem<*, RecyclerView.ViewHolder>>, notify: Boolean) {
        val start = dataSets.size
        for (item in items) {
            dataSets.add(item)
        }
        if (notify) {
            notifyItemRangeInserted(start, dataSets.size)
        }
    }

    fun removeItem(index: Int): WiDataItem<*, RecyclerView.ViewHolder>? {
        if (index > 0 && index < dataSets.size) {
            val removeItem = dataSets.removeAt(index)
            notifyItemRemoved(index)
            return removeItem
        } else {
            return null
        }
    }

    fun removeItem(item: WiDataItem<*, RecyclerView.ViewHolder>) {
        val index = dataSets.indexOf(item)
        removeItem(index)
    }

    override fun getItemViewType(position: Int): Int {
        val dataItem = dataSets.get(position)
        val type = dataItem.javaClass.hashCode()
        // 如果还没包含此类型item，添加进来
        if (typeArrays.indexOfKey(type) < 0) {
            typeArrays.put(type, dataItem)
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataItem = typeArrays.get(viewType)
        var view: View? = dataItem.getItemView(parent)
        if (view == null) {
            val itemLayoutRes = dataItem.getItemLayoutRes()
            if (itemLayoutRes < 0) {
                RuntimeException("dataItem:" + dataItem.javaClass.name + " must override getItemView or getItemLayoutRes.")
            }

            view = mInflater!!.inflate(itemLayoutRes, parent, false)
        }
        return createViewHolderInternal(dataItem.javaClass, view)
    }

    private fun createViewHolderInternal(
        javaClass: Class<WiDataItem<*, RecyclerView.ViewHolder>>,
        view: View?
    ): RecyclerView.ViewHolder {
        val genericSuperclass = javaClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val arguments = genericSuperclass.actualTypeArguments
            for (argument in arguments) {
                if (argument is Class<*> && RecyclerView.ViewHolder::class.java.isAssignableFrom(
                        argument
                    )
                ) {
                    return argument.getConstructor(View::class.java).newInstance(view) as RecyclerView.ViewHolder
                }
            }
        }
        return object : RecyclerView.ViewHolder(view!!) {}
    }

    override fun getItemCount(): Int {
        return dataSets.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }
}