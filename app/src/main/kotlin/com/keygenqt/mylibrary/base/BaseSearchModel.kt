package com.keygenqt.mylibrary.base

import androidx.annotation.LayoutRes
import com.google.gson.annotations.SerializedName
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.hal.Link

abstract class BaseSearchModel(@LayoutRes val filter: Int = R.layout.item_search) {
    @SerializedName("_links")
    var links: Map<String, Link> = hashMapOf()
}