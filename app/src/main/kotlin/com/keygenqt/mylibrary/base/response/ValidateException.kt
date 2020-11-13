package com.keygenqt.mylibrary.base.response

class ValidateException(
    val status: Int,
    override val message: String,
    val error: String,
    val errors: List<ValidateExceptionError>,
    val path: String,
    val timestamp: String,
) : RuntimeException()