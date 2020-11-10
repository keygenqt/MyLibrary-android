package com.keygenqt.mylibrary.data.services

import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.data.models.*
import com.keygenqt.mylibrary.hal.*
import com.keygenqt.mylibrary.hal.ListData.*
import kotlinx.coroutines.*

class BookService(private val api: BookApi) {

    fun getView(delegate: (ModelBook) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            api.getView().getResponse { item ->
                delegate.invoke(item)
            }
        }
    }

    fun getList(page: Int, delegate: (ListData<ModelBook>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            api.getList().getResponse { response ->
                response.type = if (page == 1) ListData.LIST_DATA_TYPE_SET else Companion.LIST_DATA_TYPE_ADD
                delegate.invoke(response)
            }
        }
    }
}