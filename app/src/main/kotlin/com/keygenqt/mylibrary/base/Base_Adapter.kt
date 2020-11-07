package com.keygenqt.mylibrary.base

import android.view.*
import androidx.annotation.*
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.*
import com.keygenqt.mylibrary.*
import com.keygenqt.mylibrary.interfaces.*
import com.keygenqt.mylibrary.utils.*
import java.util.*
import kotlin.concurrent.*

/**
 * BaseAdapterHolder is default holder for custom adapters
 *
 * @author      Vitaliy Zarubin
 * @version     %I%, %G%
 * @since       1.0
 */
class BaseAdapterHolder(
    @LayoutRes id: Int, group: ViewGroup,
    var view: View = LayoutInflater.from(group.context).inflate(id, group, false)
) : ViewHolder(view)

/**
 * BaseAdapterHolderLoading is holder for show loading page
 *
 * @author      Vitaliy Zarubin
 * @version     %I%, %G%
 * @since       1.0
 */
class BaseAdapterHolderLoading(
    group: ViewGroup,
    view: View = LayoutInflater.from(group.context)
        .inflate(R.layout.common_item_loading, group, false)
) : ViewHolder(view)

/**
 * BaseAdapter is designed for simple building of paginated lists
 *
 * @author      Vitaliy Zarubin
 * @version     %I%, %G%
 * @since       1.0
 */
abstract class BaseAdapter(
    @LayoutRes val id: Int,
    private val viewModel: ViewModelPage? = null
) :
    RecyclerView.Adapter<ViewHolder>() {

    /**
     * Counter pages
     */
    private var page = 0

    /**
     * For detect call nex page
     */
    private var timer = Timer()

    /**
     * Items
     */
    private var items = mutableListOf<Any>()

    /**
     * Method for override for bind view in custom adapter
     */
    abstract fun onBindViewHolder(holder: View, model: Any)

    /**
     * Bind view if it BaseAdapterHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is BaseAdapterHolder -> onBindViewHolder(holder.view, items[position])
        }
    }

    /**
     * Base method generate view from layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> BaseAdapterHolderLoading(parent)
            1 -> BaseAdapterHolder(id, parent)
            else -> throw RuntimeException("Only BaseAdapterHolder")
        }
    }

    /**
     * Detect call nex page
     */
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.itemViewType == 0) {
            timer = Timer()
            timer.schedule(800) {
                viewModel?.updateList(++page)
                timer.cancel()
            }
        }
    }

    /**
     * Detect call nex page
     */
    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.itemViewType == 0) {
            timer.cancel()
        }
    }

    /**
     * Get type and layout. If overflow array show loading
     */
    override fun getItemViewType(position: Int): Int {
        return when (items.size == position) {
            true -> 0
            false -> 1
        }
    }

    /**
     * Get count items, if adapter with pagination size +1
     */
    override fun getItemCount(): Int {
        return items.size + (if (viewModel != null && page > 0) 1 else 0)
    }

    /**
     * First step add items to list
     */
    fun setItems(items: List<Any>) {
        page = 1
        if (items.size < PAGE_SIZE || items.isEmpty()) {
            page = -1
        }
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    /**
     * Add items in list
     */
    fun addItems(items: List<Any>) {
        if (items.size < PAGE_SIZE || items.isEmpty()) {
            page = -1
        }
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}