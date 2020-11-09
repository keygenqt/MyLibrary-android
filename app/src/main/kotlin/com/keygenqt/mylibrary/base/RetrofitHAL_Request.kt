package com.keygenqt.mylibrary.base

import com.google.gson.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.*
import retrofit2.*
import java.io.*
import java.nio.charset.*

internal class RetrofitHALRequest<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<T, RequestBody> {

    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer: Writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }

    companion object {
        private val MEDIA_TYPE: MediaType = "application/json; charset=UTF-8".toMediaType()
        private val UTF_8: Charset = Charset.forName("UTF-8")
    }
}
