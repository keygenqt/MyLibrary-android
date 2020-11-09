package com.keygenqt.mylibrary.data.services

import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.data.models.*
import kotlinx.coroutines.*

class BookService(private val api: BookApi) {

    fun getView(delegate: (ModelBook) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            api.getView().getResponse { item ->
                delegate.invoke(item)
            }
        }
    }

    fun getList(delegate: (List<ModelBook>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            api.getList().getResponse { items ->
                delegate.invoke(items)
            }
        }
    }
}