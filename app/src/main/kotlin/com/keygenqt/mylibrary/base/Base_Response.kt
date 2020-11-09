package com.keygenqt.mylibrary.base

import android.util.*
import kotlinx.coroutines.*
import retrofit2.*

suspend fun <T> Response<T>.getResponse(delegate: (T) -> Unit) {
    withContext(Dispatchers.Main) {
        try {
            if (this@getResponse.isSuccessful) {
                this@getResponse.body()?.let {
                    delegate.invoke(this@getResponse.body()!!)
                }
            } else {
                Log.e("Application", "Error: ${this@getResponse.code()}")
            }
        } catch (e: HttpException) {
            Log.e("Application", "Exception ${e.message}")
        } catch (e: Throwable) {
            Log.e("Application", "Ooops: Something else went wrong")
        }
    }
}