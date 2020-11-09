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
    private val adapter: TypeAdapter<T>
) : Converter<ResponseBody, T> {

    companion object {
        const val API_KEY_LIST = "_embedded"
        const val API_KEY_LINKS = "_links"
        const val API_KEY_page = "page"
    }

    override fun convert(value: ResponseBody): T {
        val response = Buffer().write(value.string().let {
            return@let try {
                val json = JSONObject(it)
                if (json.has(API_KEY_LIST)) {
                    val models = json.getJSONObject(API_KEY_LIST)
                    models.keys().forEach { key ->
                        return@let models.getJSONArray(key).toString()
                    }
                    it
                } else it
            } catch (ex: Exception) {
                it
            }
        }.toByteArray(StandardCharsets.UTF_8)).asResponseBody("application/json; charset=UTF-8".toMediaType())
        val jsonReader = gson.newJsonReader(response.charStream())
        return value.use {
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            result
        }

    }
}
