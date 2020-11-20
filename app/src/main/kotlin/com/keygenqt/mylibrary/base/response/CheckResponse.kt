package com.keygenqt.mylibrary.base.response

import android.util.Log
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class CheckResponse {
    companion object {

        suspend fun Response<Void>.checkResponse(delegate: suspend () -> Unit) {
            if (this@checkResponse.isSuccessful) {
                delegate.invoke()
            } else {
                this@checkResponse.errorBody()?.let {
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
                            status = this@checkResponse.code(),
                            error = this@checkResponse.message(),
                            message = this@checkResponse.message(),
                        )
                    }
                }
                throw RuntimeException("Response api has error")
            }
        }

        suspend fun <T> Response<T>.checkResponse(delegate: suspend (T) -> Unit) {
            if (this@checkResponse.isSuccessful) {
                this@checkResponse.body()?.let {
                    delegate.invoke(it)
                } ?: run {
                    Log.e("TAG", "this@checkResponse.body() NULL")
                }
            } else {
                this@checkResponse.errorBody()?.let {
                    val jsonObject = JSONObject(it.string())
                    if (jsonObject.has("status")) {
                        if (jsonObject.getInt("status") == 422) {
                            throw Gson().fromJson(
                                jsonObject.toString(),
                                ValidateException::class.java
                            )
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