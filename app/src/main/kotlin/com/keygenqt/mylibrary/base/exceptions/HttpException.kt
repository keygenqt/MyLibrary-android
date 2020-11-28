package com.keygenqt.mylibrary.base.exceptions

data class HttpException(
    val datetime: String,
    val status: Int,
    val error: String,
    override val message: String
) : RuntimeException()