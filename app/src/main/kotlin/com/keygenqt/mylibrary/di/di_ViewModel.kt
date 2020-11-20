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

import com.keygenqt.mylibrary.ui.books.ViewBooks
import com.keygenqt.mylibrary.ui.other.ViewJoin
import com.keygenqt.mylibrary.ui.other.ViewLogin
import com.keygenqt.mylibrary.ui.other.ViewSplash
import com.keygenqt.mylibrary.ui.settings.ViewEditProfile
import org.koin.dsl.module

val moduleViewModel = module {
    factory { ViewBooks(get(), get(), get()) }
    factory { ViewSplash(get(), get(), get()) }
    factory { ViewLogin(get(), get(), get()) }
    factory { ViewJoin(get(), get(), get()) }
    factory { ViewEditProfile(get(), get(), get()) }
}