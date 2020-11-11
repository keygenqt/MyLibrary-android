/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.mylibrary.base

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.keygenqt.mylibrary.App
import kotlinx.coroutines.CoroutineExceptionHandler
import org.json.JSONObject
import retrofit2.Response
import java.net.ConnectException

class HttpException(
    val code: Int,
    override val message: String,
    val error: String,
    val path: String,
    val date: String,
) : RuntimeException()

fun getExceptionHandler(delegate: (Throwable) -> Unit): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is ConnectException -> {
                Toast.makeText(App.APP_CONTEXT, "Failed to connect", Toast.LENGTH_SHORT).show()
            }
            else -> {
                delegate.invoke(throwable)
            }
        }
    }
}

fun getExceptionHandler(error: MutableLiveData<Throwable>): CoroutineExceptionHandler {
    return getExceptionHandler {
        error.postValue(it)
    }
}

fun getExceptionHandler(): CoroutineExceptionHandler {
    return getExceptionHandler {

    }
}

suspend fun <T> Response<T>.checkResponse(delegate: suspend (T) -> Unit) {
    if (this@checkResponse.isSuccessful) {
        delegate.invoke(this@checkResponse.body()!!)
    } else {
        this@checkResponse.errorBody()?.let {
            val jsonObject = JSONObject(it.string())
            if (jsonObject.has("status")) {
                throw HttpException(
                    code = jsonObject.getInt("status"),
                    message = jsonObject.getString("message"),
                    error = jsonObject.getString("error"),
                    path = jsonObject.getString("path"),
                    date = jsonObject.getString("timestamp"),
                )
            }
        }
        throw RuntimeException("Response api has error")
    }
}