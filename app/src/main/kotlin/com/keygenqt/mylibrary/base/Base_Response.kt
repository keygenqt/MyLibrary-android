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

import android.util.*
import kotlinx.coroutines.*
import retrofit2.*

suspend fun <T> Response<T>.getResponse(delegate: (T) -> Unit) {
    withContext(Dispatchers.Main) {
        try {
            if (this@getResponse.isSuccessful) {
                this@getResponse.body()?.let {
                    delegate.invoke(this@getResponse.body()!!)
                }
            } else {
                Log.e("Application", "Error: ${this@getResponse.code()}")
            }
        } catch (e: HttpException) {
            Log.e("Application", "Exception ${e.message}")
        } catch (e: Throwable) {
            Log.e("Application", "Ooops: Something else went wrong")
        }
    }
}