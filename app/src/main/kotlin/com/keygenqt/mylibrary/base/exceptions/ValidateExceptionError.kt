package com.keygenqt.mylibrary.base.exceptions

class ValidateExceptionError(
    val codes: List<String>,
    val defaultMessage: String,
    val objectName: String,
    val field: String,
    val rejectedValue: String,
    val bindingFailure: String,
    val code: String,
)