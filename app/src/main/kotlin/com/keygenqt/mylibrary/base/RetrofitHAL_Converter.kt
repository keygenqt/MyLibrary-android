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
        return RetrofitHALResponse(gson, gson.getAdapter(TypeToken.get(type)), type.toString())
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        return RetrofitHALRequest(gson, gson.getAdapter(TypeToken.get(type)))
    }

    companion object {
        fun create(): RetrofitHALConverter {
            return RetrofitHALConverter(Gson())
        }
    }
}
