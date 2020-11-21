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
import com.facebook.stetho.Stetho
import com.keygenqt.mylibrary.base.BaseExceptionHandler
import com.keygenqt.mylibrary.di.moduleOther
import com.keygenqt.mylibrary.di.moduleRetrofit
import com.keygenqt.mylibrary.di.moduleViewModel
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    private val exceptionHandler: BaseExceptionHandler by inject()

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                moduleOther,
                moduleRetrofit,
                moduleViewModel
            )
        }
        registerActivityLifecycleCallbacks(exceptionHandler)
    }
}