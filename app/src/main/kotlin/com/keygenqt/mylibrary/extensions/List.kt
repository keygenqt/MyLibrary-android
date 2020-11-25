package com.keygenqt.mylibrary.extensions

import com.keygenqt.mylibrary.hal.API_KEY_SELF
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.hal.ListData

fun <T> List<T>.toListData(key: String, self: Link): ListData<T> {
    return ListData(
        embedded = hashMapOf(key to this),
        links = hashMapOf(API_KEY_SELF to self)
    )
}