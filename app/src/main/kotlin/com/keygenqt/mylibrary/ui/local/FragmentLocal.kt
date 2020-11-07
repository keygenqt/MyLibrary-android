package com.keygenqt.mylibrary.ui.local

import android.view.*
import androidx.core.content.*
import androidx.fragment.app.*
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.base.BaseListData.Companion.LIST_DATA_TYPE_SET
import com.keygenqt.mylibrary.data.*
import kotlinx.android.synthetic.main.common_fragment_list.view.*

@FragmentTitle("My Library")
class FragmentLocal : BaseFragment(R.layout.common_fragment_list) {

    private val viewLocal: ViewLocal by viewModels()

    override fun isBottomNavigation(): Boolean {
        return true
    }

    override fun onCreateView() {
        initView {
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = AdapterLocal(R.layout.item_book_list, viewLocal)
            refresh.setColorSchemeColors(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            refresh.setOnRefreshListener { viewLocal.updateList() }

            setItems(ModelBook.findAll())

            viewLocal.items.observe(viewLifecycleOwner, { (items, type) ->
                if (type == LIST_DATA_TYPE_SET) {
                    setItems(items)
                } else {
                    addAdapterItems(items)
                }
            })
            viewLocal.loading.observe(viewLifecycleOwner, { status ->
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(FragmentLocalDirections.actionFragmentLocalToFragmentSettings())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}