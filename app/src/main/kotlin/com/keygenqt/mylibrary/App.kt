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

package com.keygenqt.mylibrary

import android.app.Application
import android.util.Log
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.data.ModelBook
import com.keygenqt.mylibrary.ui.local.ViewLocal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

/**
 * App class android application
 */
class App : Application() {

    interface BookApi {
        @GET("/books/1") suspend fun getView(): Response<ModelBook>
    }

    class BookService(private val api: BookApi) {
        fun getView(delegate: (ModelBook) -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = api.getView()
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                delegate.invoke(it)
                            }
                        } else {
                            Log.e("Application", "Error: ${response.code()}")
                        }
                    } catch (e: HttpException) {
                        Log.e("Application", "Exception ${e.message}")
                    } catch (e: Throwable) {
                        Log.e("Application", "Ooops: Something else went wrong")
                    }
                }
            }
        }
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
                            Log.i("HttpLoggingInterceptor", message)
                        }
                    }).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor {
                    val original = it.request()
                    val request = original.newBuilder()
                        .header("Authorization", sharedPreferences.token ?: "")
                        .method(original.method, original.body)
                        .build()
                    it.proceed(request)
                }
                .build())
            .build()

    }

    fun provideNetworkApi(retrofit: Retrofit): BookApi =
        retrofit.create(BookApi::class.java)

    fun provideNetworkService(api: BookApi): BookService = BookService(api)

    /**
     * For initialization dependencies
     */
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                module {
                    single { BaseSharedPreferences(androidContext().getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)) }
                },
                module {
                    factory { provideRetrofit(get()) }
                    single { provideNetworkService(provideNetworkApi(get())) }
                },
                module {
                    factory { ViewLocal(get(), get()) }
                }
            )
        }
    }
}