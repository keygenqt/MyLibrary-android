package com.keygenqt.mylibrary.base

import com.google.gson.*
import com.google.gson.reflect.*
import okhttp3.*
import retrofit2.*
import java.lang.reflect.*

class RetrofitHALConverter(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return RetrofitHALResponse(gson, adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return RetrofitHALRequest(gson, adapter)
    }

    companion object {
        fun create(): RetrofitHALConverter {
            return RetrofitHALConverter(Gson())
        }
    }
}
