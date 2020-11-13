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

package com.keygenqt.mylibrary.base.response

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import org.json.JSONObject
import retrofit2.Response

class BaseResponseError {
    companion object {
        val error: MutableLiveData<Throwable> = MutableLiveData()

        fun getExceptionHandler(delegate: (Throwable) -> Unit): CoroutineExceptionHandler {
            return CoroutineExceptionHandler { _, throwable ->
                error.postValue(throwable)
                delegate.invoke(throwable)
            }
        }

        fun getExceptionHandler(error: MutableLiveData<Throwable>): CoroutineExceptionHandler {
            return getExceptionHandler {
                error.postValue(it)
            }
        }

        fun getExceptionHandler(): CoroutineExceptionHandler {
            return getExceptionHandler {}
        }

        suspend fun <T> Response<T>.checkResponse(delegate: suspend (T) -> Unit) {
            if (this@checkResponse.isSuccessful) {
                delegate.invoke(this@checkResponse.body()!!)
            } else {
                this@checkResponse.errorBody()?.let {
                    val jsonObject = JSONObject(it.string())
                    if (jsonObject.has("status")) {
                        if (jsonObject.getInt("status") == 422) {
                            throw Gson().fromJson(jsonObject.toString(), ValidateException::class.java)
                        } else {
                            throw Gson().fromJson(jsonObject.toString(), HttpException::class.java)
                        }
                    }
                }
                throw RuntimeException("Response api has error")
            }
        }
    }
}