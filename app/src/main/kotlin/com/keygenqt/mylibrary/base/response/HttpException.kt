package com.keygenqt.mylibrary.base.response

class HttpException(
    val datetime: String? = null,
    val status: Int,
    val error: String,
    override val message: String
) : RuntimeException()