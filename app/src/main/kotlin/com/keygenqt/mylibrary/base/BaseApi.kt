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

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface BaseApi {
    @GET
    fun get(@Url link: String): Call<JsonElement>

    @POST
    fun post(@Url link: String, @Body fields: JsonObject): Call<JsonElement>

    @POST
    fun postRequestBody(@Url link: String, @Body bites: RequestBody): Call<JsonElement>

    @PUT
    fun put(@Url link: String, @Body fields: JsonObject): Call<JsonElement>

    @DELETE
    fun delete(@Url link: String): Call<JsonElement>
}