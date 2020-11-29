package com.keygenqt.mylibrary.base

class LiveDataEvent<T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun peekContentHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T? = content
}