package com.keygenqt.mylibrary.hal

import com.google.gson.annotations.*

data class Page(
    @SerializedName("size")
    var size: String = "",

    @SerializedName("totalElements")
    var totalElements: String = "",

    @SerializedName("totalPages")
    var totalPages: String = "",

    @SerializedName("number")
    var number: String = "",
)