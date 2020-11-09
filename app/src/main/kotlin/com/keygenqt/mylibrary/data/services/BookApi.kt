package com.keygenqt.mylibrary.data.services

import com.keygenqt.mylibrary.data.models.*
import retrofit2.*
import retrofit2.http.*

interface BookApi {
    @GET("/books/2") suspend fun getView(): Response<ModelBook>

    @GET("/books") suspend fun getList(): Response<List<ModelBook>>
}