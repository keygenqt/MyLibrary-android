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

package com.keygenqt.mylibrary.data.services

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.keygenqt.mylibrary.base.exceptions.HttpException
import com.keygenqt.mylibrary.base.exceptions.ValidateException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommonQuery(val api: CommonApi) {

    inline fun <reified T> getAsync(coroutineScope: CoroutineScope, link: String): Deferred<T> {
        return coroutineScope.async<T> {
            val body = api.get(link).awaitResponse().checkResponse()
            Gson().fromJson(body, T::class.java)
        }
    }

    inline fun <reified T> postAsync(coroutineScope: CoroutineScope, link: String, params: JsonObject): Deferred<T> {
        return coroutineScope.async<T> {
            val body = api.post(link, params).awaitResponse().checkResponse()
            Gson().fromJson(body, T::class.java)
        }
    }

    inline fun <reified T> putAsync(coroutineScope: CoroutineScope, link: String, params: JsonObject): Deferred<T> {
        return coroutineScope.async<T> {
            val body = api.put(link, params).awaitResponse().checkResponse()
            Gson().fromJson(body, T::class.java)
        }
    }

    fun <T> Response<T>.checkResponse(): T {
        if (this.isSuccessful) {
            this.body()?.let {
                return it
            } ?: run {
                Log.e("TAG", "this@checkResponse.body() NULL")
            }
        } else {
            this.errorBody()?.let {
                try {
                    val jsonObject = JSONObject(it.string())
                    if (jsonObject.has("status")) {
                        if (jsonObject.getInt("status") == 422) {
                            throw Gson().fromJson(jsonObject.toString(), ValidateException::class.java)
                        } else {
                            throw Gson().fromJson(jsonObject.toString(), HttpException::class.java)
                        }
                    }
                } catch (ex: JSONException) {
                    throw HttpException(
                        datetime = SimpleDateFormat("d MMM yyyy", Locale.US).format(Date()),
                        status = this@checkResponse.code(),
                        error = this@checkResponse.message(),
                        message = this@checkResponse.message(),
                    )
                }
            }
        }
        throw RuntimeException("Response api has error")
    }
}