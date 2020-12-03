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

package com.keygenqt.mylibrary.di

import android.util.Log
import com.keygenqt.mylibrary.base.BaseApi
import com.keygenqt.mylibrary.base.BaseQuery
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit

val moduleRetrofit = module {
    factory { provideRetrofit(get()) }
    single { provideService(provideApi(get())) }
}

fun provideRetrofit(sharedPreferences: BaseSharedPreferences): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://192.168.1.68:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor { message -> Log.i("HttpLoggingInterceptor", message) }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor {
                val original = it.request()
                val request = original.newBuilder()
                    .header("Authorization", sharedPreferences.token ?: "")
                    .header("Accept-Language", Locale.getDefault().toLanguageTag())
                    .method(original.method, original.body)
                    .build()
                it.proceed(request)
            }
            .build())
        .build()

}

fun provideApi(retrofit: Retrofit): BaseApi = retrofit.create(BaseApi::class.java)

fun provideService(api: BaseApi): BaseQuery = BaseQuery(api)