package com.keygenqt.mylibrary.base.response

class HttpException(
    val status: Int,
    override val message: String,
    val error: String,
    val path: String,
    val timestamp: String,
) : RuntimeException()