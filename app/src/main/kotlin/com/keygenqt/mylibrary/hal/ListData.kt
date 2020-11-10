package com.keygenqt.mylibrary.hal

import com.google.gson.annotations.*

data class ListData<T>(
    var type: Int = LIST_DATA_TYPE_SET,

    @SerializedName("_embedded")
    var embedded: HashMap<String, List<T>> = hashMapOf(),

    @SerializedName("_links")
    var links: HashMap<String, Link> = hashMapOf(),

    @SerializedName("page")
    var page: Page? = null
) {

    val items: List<T>
        get() {
            embedded.forEach { return it.value }
            return emptyList()
        }

    companion object {
        const val LIST_DATA_TYPE_ADD = 1
        const val LIST_DATA_TYPE_SET = 2
    }
}