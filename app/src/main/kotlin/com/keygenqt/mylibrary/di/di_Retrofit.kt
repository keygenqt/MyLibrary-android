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
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.data.services.BookApi
import com.keygenqt.mylibrary.data.services.BookService
import com.keygenqt.mylibrary.data.services.OtherApi
import com.keygenqt.mylibrary.data.services.OtherService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val moduleRetrofit = module {
    factory { provideRetrofit(get()) }
    single { provideBookService(provideBookApi(get())) }
    single { provideOtherService(provideOtherApi(get()), get()) }
}

fun provideRetrofit(sharedPreferences: BaseSharedPreferences): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://192.168.1.68:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor(
                object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
//                        Log.i("HttpLoggingInterceptor", message)
                    }
                }).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor {
                val original = it.request()
                val request = original.newBuilder()
                    .header("authorization", sharedPreferences.token ?: "")
                    .header("uid", sharedPreferences.uid)
                    .method(original.method, original.body)
                    .build()
                it.proceed(request)
            }
            .build())
        .build()

}

fun provideBookApi(retrofit: Retrofit): BookApi = retrofit.create(BookApi::class.java)

fun provideBookService(api: BookApi): BookService = BookService(api)

fun provideOtherApi(retrofit: Retrofit): OtherApi = retrofit.create(OtherApi::class.java)

fun provideOtherService(api: OtherApi, preferences: BaseSharedPreferences): OtherService = OtherService(api, preferences)