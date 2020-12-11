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

package com.keygenqt.mylibrary.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.ui.books.FragmentBook
import com.keygenqt.mylibrary.ui.settings.FragmentLicenses
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor

// run stack deep link for NavDeepLinkRequest
private fun NavController.conf(context: Context, uri: Uri) {
    uri.path?.apply {
        when {
            contains("""\/${ModelBook.API_KEY}\/\d+""".toRegex()) -> stack(FragmentBook::class, uri)
            this == context.getString(R.string.link_licenses).toUri().path -> stack(FragmentLicenses::class, uri)
        }
    }
}

fun NavController.navigateUri(context: Context, intent: Intent) {
    intent.extras?.getString("uri")?.let { link ->
        navigateUri(context, link)
    }
}

fun NavController.navigateUri(context: Context, link: String) {
    this.apply {
        conf(context, link.toUri())
        navigate(NavDeepLinkRequest.Builder.fromUri(link.toUri()).build())
    }
}

private fun NavController.stack(kClass: KClass<out BaseFragment<*>>, uri: Uri) {
    kClass.memberFunctions.forEach { m ->
        if (!m.hasAnnotation<BaseFragment.UpStack>()) return@forEach
        m.call(kClass.primaryConstructor!!.call(), uri, this)
    }
}