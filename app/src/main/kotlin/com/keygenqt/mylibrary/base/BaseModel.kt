package com.keygenqt.mylibrary.base

import com.google.gson.annotations.SerializedName
import com.keygenqt.mylibrary.hal.API_KEY_SELF
import com.keygenqt.mylibrary.hal.Link

open class BaseModel {

    @SerializedName("_links")
    var links: Map<String, Link> = hashMapOf()

    open fun baseId(): Long {
        return 0
    }

    fun getLink(key: String): Link {
        if (links.containsKey(key)) {
            return links.getValue(key)
        }
        throw RuntimeException("API key $key not found")
    }

    var selfLink: String = ""
        get() {
            if (field.isEmpty() && links.containsKey(API_KEY_SELF)) {
                return links.getValue(API_KEY_SELF).value
            }
            return field
        }
}