package com.keygenqt.mylibrary.ui.settings.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.keygenqt.mylibrary.R
import kotlinx.android.synthetic.main.view_avatar.view.imageViewAvatar
import kotlinx.android.synthetic.main.view_avatar.view.textViewName

class DotIndicatorPagerAdapter(private val items: LinkedHashMap<Int, Int>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = LayoutInflater.from(container.context).inflate(R.layout.view_avatar, container, false)
        items.keys.toList()[position].let { key ->
            item.imageViewAvatar.setImageResource(key)
            item.textViewName.text = container.resources.getString(items[key]!!)
        }
        container.addView(item)
        return item
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view === any
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }
}