package com.keygenqt.mylibrary.base

import com.google.gson.*
import com.google.gson.stream.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.*
import org.json.*
import retrofit2.*
import java.nio.charset.*

internal class RetrofitHALResponse<T>(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>,
    private val typeName: String,
) : Converter<ResponseBody, T> {

    companion object {
        const val API_KEY_LIST = "_embedded"
        const val API_KEY_PAGE = "page"
    }

    override fun convert(value: ResponseBody): T {
        val jsonReader = gson.newJsonReader(checkResponse(value).charStream())
        return value.use {
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            result
        }
    }

    private fun checkResponse(value: ResponseBody): ResponseBody {
        return if (typeName.contains("java.util.List")) {
            Buffer().write(value.string().let write@{
                return@write try {
                    val json = JSONObject(it)
                    if (json.has(API_KEY_LIST)) {
                        json.getJSONObject(API_KEY_LIST).keys().forEach { key ->
                            return@write json.getJSONObject(API_KEY_LIST).getJSONArray(key).toString()
                        }
                    }
                    it
                } catch (ex: Exception) {
                    it
                }
            }.toByteArray(StandardCharsets.UTF_8)).asResponseBody("application/json; charset=UTF-8".toMediaType())
        } else {
            value
        }
    }
}
