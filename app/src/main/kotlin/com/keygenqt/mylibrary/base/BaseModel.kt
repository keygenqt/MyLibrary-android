package com.keygenqt.mylibrary.base

import com.google.gson.annotations.SerializedName
import com.keygenqt.mylibrary.hal.Link

open class BaseModel {

    @SerializedName("_links")
    var links: Map<String, Link> = hashMapOf()

    fun getLink(key: String): Link {
        if (links.containsKey(key)) {
            return links.getValue(key)
        }
        throw RuntimeException("API key $key not found")
    }
}