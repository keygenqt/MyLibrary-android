package com.keygenqt.mylibrary.ui.online

import android.view.*
import androidx.core.content.*
import androidx.fragment.app.*
import androidx.recyclerview.widget.*
import com.keygenqt.mylibrary.*
import com.keygenqt.mylibrary.R.*
import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.base.BaseListData.Companion.LIST_DATA_TYPE_SET
import com.keygenqt.mylibrary.data.*
import kotlinx.android.synthetic.main.common_fragment_list.view.*

@FragmentTitle("Users Libs")
class FragmentOnline : BaseFragment(R.layout.common_fragment_list) {

    private val viewOnline: ViewOnline by viewModels()

    override fun isBottomNavigation(): Boolean {
        return true
    }

    override fun onCreateView() {
        initView {
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = AdapterOnline(layout.item_book_list, viewOnline)
            refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            refresh.setOnRefreshListener { viewOnline.updateList() }

            setItems(ModelBook.findAll())

            viewOnline.items.observe(viewLifecycleOwner, { (items, type) ->
                if (type == LIST_DATA_TYPE_SET) {
                    setItems(items)
                } else {
                    addAdapterItems(items)
                }
            })
            viewOnline.loading.observe(viewLifecycleOwner, { status ->
                refresh.isRefreshing = status
            })
        }
    }

    private fun setItems(items: List<Any>) {
        initView {
            notFound.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            (recyclerView.adapter as BaseAdapter).setItems(items)
            recyclerView.smoothScrollToPosition(0)
        }
    }

    private fun addAdapterItems(items: List<Any>) {
        initView {
            (recyclerView.adapter as BaseAdapter).addItems(items)
        }
    }
}