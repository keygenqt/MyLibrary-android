package com.keygenqt.mylibrary.base.response

import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Response

class CheckResponse {
    companion object {
        suspend fun <T> Response<T>.checkResponse(delegate: suspend (T) -> Unit) {
            if (this@checkResponse.isSuccessful) {
                delegate.invoke(this@checkResponse.body()!!)
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
                            Log.e("TAG", jsonObject.toString())
                            throw Gson().fromJson(jsonObject.toString(), HttpException::class.java)
                        }
                    }
                }
                throw RuntimeException("Response api has error")
            }
        }
    }
}