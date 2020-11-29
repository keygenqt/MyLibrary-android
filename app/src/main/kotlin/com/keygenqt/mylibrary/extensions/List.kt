package com.keygenqt.mylibrary.extensions

import com.keygenqt.mylibrary.data.models.ModelBook

fun List<Any>.findIndex(item: ModelBook): Int {
    this.forEachIndexed { index, any ->
        if (any is ModelBook && item.id == any.id) {
            return index
        }
    }
    return 0
}