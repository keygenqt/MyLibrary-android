package com.keygenqt.mylibrary.data.services

import com.keygenqt.mylibrary.data.models.*
import com.keygenqt.mylibrary.hal.*
import retrofit2.*
import retrofit2.http.*

interface BookApi {
    @GET("/books/2") suspend fun getView(): Response<ModelBook>

    @GET("/books") suspend fun getList(): Response<ListData<ModelBook>>
}