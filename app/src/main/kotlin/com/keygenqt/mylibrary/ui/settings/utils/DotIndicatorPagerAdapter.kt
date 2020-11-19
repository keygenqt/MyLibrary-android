package com.keygenqt.mylibrary.ui.settings.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.keygenqt.mylibrary.R
import kotlinx.android.synthetic.main.material_page.view.*

class DotIndicatorPagerAdapter : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = LayoutInflater.from(container.context).inflate(R.layout.material_page, container, false)
        item.imageViewAvatar.setImageResource(listOf(
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5
        ).random())
        item.textViewName.setText(listOf(
            "Sad",
            "Cheerful",
            "Happy",
            "Evil"
        ).random())
        container.addView(item)
        return item
    }

    override fun getCount(): Int {
        return 15
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view === any
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }
}